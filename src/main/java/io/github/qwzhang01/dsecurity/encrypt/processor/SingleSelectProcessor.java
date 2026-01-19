package io.github.qwzhang01.dsecurity.encrypt.processor;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Single table select processor for SQL modification.
 *
 * <p>This processor handles single table SELECT queries by adding table name
 * prefix
 * to all columns in both SELECT clause and WHERE conditions.</p>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * Input:  SELECT id, name FROM user WHERE status = 1
 * Output: SELECT user.id, user.name FROM user WHERE user.status = 1
 * </pre>
 *
 * @author avinzhang
 */
public class SingleSelectProcessor {
    private static final Logger log =
            LoggerFactory.getLogger(SingleSelectProcessor.class);

    private SingleSelectProcessor() {
    }

    public static SingleSelectProcessor getInstance() {
        return SingleSelectProcessor.Holder.INSTANCE;
    }

    /**
     * Apply single table select processing to add table prefix to columns.
     *
     * @param invocation the method invocation containing SQL to modify
     */
    public void apply(Invocation invocation) {
        try {
            StatementHandler statementHandler =
                    (StatementHandler) invocation.getTarget();
            BoundSql boundSql = statementHandler.getBoundSql();
            String originalSql = boundSql.getSql();

            String modifiedSql = process(originalSql);

            if (modifiedSql != null && !modifiedSql.equals(originalSql)) {
                Field field = BoundSql.class.getDeclaredField("sql");
                field.setAccessible(true);
                field.set(boundSql, modifiedSql);
                log.debug("SQL modified: {} -> {}", originalSql, modifiedSql);
            }
        } catch (Exception e) {
            log.error("Failed to process single select SQL", e);
        }
    }

    /**
     * Process SQL string to add table prefix to columns.
     *
     * @param sql the original SQL string
     * @return the modified SQL string with table prefixes, or null if not
     * applicable
     */
    public String process(String sql) {
        if (sql == null || sql.isEmpty()) {
            return null;
        }

        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (!(statement instanceof Select)) {
                log.debug("Not a SELECT statement, skipping");
                return null;
            }

            Select select = (Select) statement;
            if (!(select.getPlainSelect() != null)) {
                log.debug("Not a plain SELECT, skipping");
                return null;
            }

            PlainSelect plainSelect = select.getPlainSelect();

            // Check if it's a single table query (no joins)
            if (plainSelect.getJoins() != null && !plainSelect.getJoins().isEmpty()) {
                log.debug("Query has joins, not a single table query, " +
                        "skipping");
                return null;
            }

            // Get the table name
            if (!(plainSelect.getFromItem() instanceof Table)) {
                log.debug("FromItem is not a Table, skipping");
                return null;
            }

            Table table = (Table) plainSelect.getFromItem();
            String tableName = table.getAlias() != null ?
                    table.getAlias().getName() : table.getName();

            if (tableName == null || tableName.isEmpty()) {
                log.debug("Table name is empty, skipping");
                return null;
            }

            // Process SELECT items
            processSelectItems(plainSelect.getSelectItems(), tableName);

            // Process WHERE clause
            Expression where = plainSelect.getWhere();
            if (where != null) {
                processExpression(where, tableName);
            }

            // Process ORDER BY
            if (plainSelect.getOrderByElements() != null) {
                plainSelect.getOrderByElements().forEach(orderByElement -> {
                    if (orderByElement.getExpression() instanceof Column) {
                        Column column = (Column) orderByElement.getExpression();
                        addTablePrefixToColumn(column, tableName);
                    }
                });
            }

            // Process GROUP BY
            if (plainSelect.getGroupBy() != null &&
                    plainSelect.getGroupBy().getGroupByExpressionList() != null) {
                plainSelect.getGroupBy().getGroupByExpressionList().forEach(expr -> {
                    if (expr instanceof Column) {
                        addTablePrefixToColumn((Column) expr, tableName);
                    }
                });
            }

            // Process HAVING
            Expression having = plainSelect.getHaving();
            if (having != null) {
                processExpression(having, tableName);
            }

            return statement.toString();

        } catch (Exception e) {
            log.error("Failed to parse SQL: {}", sql, e);
            return null;
        }
    }

    /**
     * Process SELECT items to add table prefix.
     */
    private void processSelectItems(List<SelectItem<?>> selectItems,
                                    String tableName) {
        if (selectItems == null) {
            return;
        }

        List<SelectItem<?>> newSelectItems = new ArrayList<>();
        boolean modified = false;

        for (SelectItem<?> selectItem : selectItems) {
            Expression expression = selectItem.getExpression();

            // Handle SELECT * -> SELECT table.*
            if (expression instanceof AllColumns) {
                Table tableRef = new Table(tableName);
                AllTableColumns allTableColumns = new AllTableColumns(tableRef);
                newSelectItems.add(new SelectItem<>(allTableColumns));
                modified = true;
            } else if (expression instanceof Column) {
                Column column = (Column) expression;
                addTablePrefixToColumn(column, tableName);
                newSelectItems.add(selectItem);
            } else if (expression != null) {
                // Handle expressions like functions, case statements, etc.
                processExpression(expression, tableName);
                newSelectItems.add(selectItem);
            } else {
                newSelectItems.add(selectItem);
            }
        }

        // Replace select items if we converted * to table.*
        if (modified) {
            selectItems.clear();
            selectItems.addAll(newSelectItems);
        }
    }

    /**
     * Process expression recursively to add table prefix to all columns.
     */
    private void processExpression(Expression expression, String tableName) {
        if (expression == null) {
            return;
        }

        expression.accept(new ExpressionVisitorAdapter<Void>() {
            @Override
            public <S> Void visit(Column column, S context) {
                addTablePrefixToColumn(column, tableName);
                return null;
            }
        });
    }

    /**
     * Add table prefix to a column if not already present.
     */
    private void addTablePrefixToColumn(Column column, String tableName) {
        if (column == null) {
            return;
        }

        // Skip if column already has a table prefix
        if (column.getTable() != null && column.getTable().getName() != null) {
            return;
        }

        // Skip null column name
        String columnName = column.getColumnName();
        if (columnName == null) {
            return;
        }

        // Add table prefix (including * -> table.*)
        Table tableRef = new Table(tableName);
        column.setTable(tableRef);
    }

    private static final class Holder {
        private static final SingleSelectProcessor INSTANCE =
                new SingleSelectProcessor();
    }
}