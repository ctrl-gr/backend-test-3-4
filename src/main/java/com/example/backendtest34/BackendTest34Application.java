package com.example.backendtest34;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class BackendTest34Application {
    private static final Logger log = LoggerFactory.getLogger(BackendTest34Application.class);


    public static void main(String[] args) {
        var context = SpringApplication.run(BackendTest34Application.class, args);

        //table already created in my db
        var jdbcTemplate = context.getBean(JdbcTemplate.class);


        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "A", 2021, 500);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "B", 2021, 1000);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "A", 2021, 500);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "B", 2021, 1000);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "C", 2022, 900);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "A", 2022, 1200);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "B", 2021, 600);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "A", 2022, 900);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "B", 2021, 500);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "C", 2021, 1000);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "C", 2022, 700);
        jdbcTemplate.update("INSERT INTO sales_table (username, year, sales) VALUES (?, ?, ?)", "B", 2021, 500);


        List<Map<String, Object>> results = jdbcTemplate.queryForList(
                "SELECT username, year, total_sales, total_sales - COALESCE((SELECT SUM(sales) FROM sales_table WHERE username = t.username AND year = t.year - 1 GROUP BY username, year), 0) AS variation " +
                        "FROM (SELECT username, year, SUM(sales) AS total_sales FROM sales_table GROUP BY username, year) t " +
                        "ORDER BY username, year"
        );

        for (Map<String, Object> row : results) {
            log.info("User: {}, Year: {}, Total Sales: {}, Variation: {}", row.get("username"), row.get("year"), row.get("total_sales"), row.get("variation"));
        }

        context.close();
    }

}

