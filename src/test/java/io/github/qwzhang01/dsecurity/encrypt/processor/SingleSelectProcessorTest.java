package io.github.qwzhang01.dsecurity.encrypt.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for SingleSelectProcessor.
 */
public class SingleSelectProcessorTest {

    private final SingleSelectProcessor processor =
            SingleSelectProcessor.getInstance();

    @Test
    public void testSimpleSelect() {
        String sql = "SELECT id, name FROM user WHERE status = 1";
        String result = processor.process(sql);

        assertNotNull(result);
        assertTrue(result.contains("user.id"));
        assertTrue(result.contains("user.name"));
        assertTrue(result.contains("user.status"));
        System.out.println("Input:  " + sql);
        System.out.println("Output: " + result);
    }

    @Test
    public void testSelectStar() {
        String sql = "SELECT * FROM user WHERE id = 1";
        String result = processor.process(sql);

        assertNotNull(result);
        assertTrue(result.contains("user.*"));
        assertTrue(result.contains("user.id"));
        System.out.println("Input:  " + sql);
        System.out.println("Output: " + result);
    }

    @Test
    public void testSelectWithOrderBy() {
        String sql = "SELECT id, name FROM user WHERE status = 1 ORDER BY " +
                "create_time DESC";
        String result = processor.process(sql);

        assertNotNull(result);
        assertTrue(result.contains("user.id"));
        assertTrue(result.contains("user.name"));
        assertTrue(result.contains("user.status"));
        assertTrue(result.contains("user.create_time"));
        System.out.println("Input:  " + sql);
        System.out.println("Output: " + result);
    }

    @Test
    public void testSelectWithGroupBy() {
        String sql = "SELECT status, COUNT(*) FROM user GROUP BY status";
        String result = processor.process(sql);

        assertNotNull(result);
        assertTrue(result.contains("user.status"));
        System.out.println("Input:  " + sql);
        System.out.println("Output: " + result);
    }

    @Test
    public void testSelectWithAlias() {
        String sql = "SELECT id, name FROM user u WHERE status = 1";
        String result = processor.process(sql);

        assertNotNull(result);
        assertTrue(result.contains("u.id"));
        assertTrue(result.contains("u.name"));
        assertTrue(result.contains("u.status"));
        System.out.println("Input:  " + sql);
        System.out.println("Output: " + result);
    }

    @Test
    public void testSelectWithMultipleConditions() {
        String sql = "SELECT id, name, email FROM user WHERE status = 1 AND " +
                "age > 18 AND name LIKE '%test%'";
        String result = processor.process(sql);

        assertNotNull(result);
        assertTrue(result.contains("user.id"));
        assertTrue(result.contains("user.name"));
        assertTrue(result.contains("user.email"));
        assertTrue(result.contains("user.status"));
        assertTrue(result.contains("user.age"));
        System.out.println("Input:  " + sql);
        System.out.println("Output: " + result);
    }

    @Test
    public void testJoinQueryShouldReturnNull() {
        String sql = "SELECT u.id, o.name FROM user u JOIN orders o ON u.id =" +
                " o.user_id";
        String result = processor.process(sql);

        assertNull(result);
        System.out.println("Input:  " + sql);
        System.out.println("Output: null (JOIN query, not processed)");
    }

    @Test
    public void testInsertShouldReturnNull() {
        String sql = "INSERT INTO user (id, name) VALUES (1, 'test')";
        String result = processor.process(sql);

        assertNull(result);
        System.out.println("Input:  " + sql);
        System.out.println("Output: null (INSERT statement, not processed)");
    }

    @Test
    public void testColumnAlreadyHasTablePrefix() {
        String sql = "SELECT user.id, name FROM user WHERE user.status = 1";
        String result = processor.process(sql);

        assertNotNull(result);
        assertTrue(result.contains("user.id"));
        assertTrue(result.contains("user.name"));
        assertTrue(result.contains("user.status"));
        System.out.println("Input:  " + sql);
        System.out.println("Output: " + result);
    }

    @Test
    public void testSelectWithHaving() {
        String sql = "SELECT status, COUNT(*) as cnt FROM user GROUP BY " +
                "status HAVING cnt > 5";
        String result = processor.process(sql);

        assertNotNull(result);
        assertTrue(result.contains("user.status"));
        System.out.println("Input:  " + sql);
        System.out.println("Output: " + result);
    }

    @Test
    public void testNullSql() {
        String result = processor.process(null);
        assertNull(result);
    }

    @Test
    public void testEmptySql() {
        String result = processor.process("");
        assertNull(result);
    }
}
