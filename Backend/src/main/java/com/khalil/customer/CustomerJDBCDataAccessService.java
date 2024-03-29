package com.khalil.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate,
                                         CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT * FROM customer;
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT * FROM customer
                WHERE id = ?;
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCostumer(Customer customer) {
        var sql = """
                INSERT INTO customer (name, email, age)
                VALUES (?, ?, ?);
                """;
        int result = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT COUNT(id) FROM customer
                WHERE email = ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerById(Long id) {
        var sql = """
                SELECT COUNT(id) FROM customer
                WHERE id = ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCurstomerWithId(Long id) {
        var sql = """
                DELETE FROM customer
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomerWithId(Customer update) {
        if (update.getName() != null){
            String sql = """
                    UPDATE customer
                    set
                    name = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql, update.getName(), update.getId());
        }

        if (update.getEmail() != null){
            String sql = """
                    UPDATE customer
                    set
                    email = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql, update.getEmail(), update.getId());
        }

        if (update.getAge() != null){
            String sql = """
                    UPDATE customer
                    set
                    age = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql, update.getAge(), update.getId());
        }
    }
}
