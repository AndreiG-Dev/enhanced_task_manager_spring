package com.andreig.enhanced_task_manager_spring.dao;

import com.andreig.enhanced_task_manager_spring.model.Task;
import com.andreig.enhanced_task_manager_spring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = "prototype")
public class UserDAO implements UserDAOService {

    public final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUser(User user){
        if( !jdbcTemplate.query("SELECT * FROM users WHERE username = ?",new BeanPropertyRowMapper<>(User.class),
                user.getUserName()).isEmpty()){
            System.out.println(ALREADY_EXISTS);
            return;
        }
        jdbcTemplate.update("INSERT INTO users VALUES(?, ?, ?)",
                user.getFirstName(), user.getLastName(), user.getUserName());
    }

    public void addTeamId(String userName, int id){
        jdbcTemplate.update("UPDATE users SET teamId=?, WHERE userName=?", id, userName);
    }

    public void assignTaskToTeam(int teamId, Task task){

    }

    @Override
    public List<User> showAllUsers(){
        return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void assignTask(String userName, Task task) {
        String currentTasks = "";
        jdbcTemplate.query("SELECT tasks FROM user WHERE userName=?", new BeanPropertyRowMapper<>(String.class),
                userName);
        jdbcTemplate.update("UPDATE user SET tasks=?, WHERE userName=?", currentTasks.toString(), userName);
    }

    @Override
    public List<Task> getUserTasks(String userName){
        return jdbcTemplate.query("SELECT tasks FROM user WHERE userName = ?", new BeanPropertyRowMapper<>(),
                userName);
    }
}
