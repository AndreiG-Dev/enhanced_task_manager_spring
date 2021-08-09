package com.andreig.enhanced_task_manager_spring.dao;

import com.andreig.enhanced_task_manager_spring.model.Task;
import com.andreig.enhanced_task_manager_spring.model.User;
import com.andreig.enhanced_task_manager_spring.model.UserTaskNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
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
        jdbcTemplate.update("INSERT INTO users VALUES(?, ?, ?, ?)", getMaxUserId()+1,
                user.getFirstName(), user.getLastName(), user.getUserName());
        System.out.println(USER_ADDED);
    }

    public void addTeamId(String userName, int id){
        jdbcTemplate.update("UPDATE users SET teamId=? WHERE userName=?", id, userName);
    }

    public void assignTaskToTeam(int teamId, Task task){
        List<User> team = jdbcTemplate.query("SELECT userid, firstname, lastname, username FROM users WHERE teamid = ?",
                new BeanPropertyRowMapper<>(User.class), teamId);

        team.forEach(user -> assignTask(user.getUserName(), task));
    }

    @Override
    public List<User> showAllUsers(){
        return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
    }

    public int getMaxUserId(){
        List<User> userList = jdbcTemplate.query("SELECT userid FROM users", new BeanPropertyRowMapper<>(User.class));
        if (userList.isEmpty()){
            return 0;
        }
        return userList.stream().max(Comparator.comparingInt(User::getUserId)).get().getUserId();
    }

    public int getMaxTaskId(){
        List<Task> tasks = jdbcTemplate.query("SELECT taskid FROM tasks", new BeanPropertyRowMapper<>(Task.class));
        if (tasks.isEmpty()){
            return 0;
        }
        return tasks.stream().max(Comparator.comparingInt(Task::getTaskId)).get().getTaskId();
    }

    @Override
    public void assignTask(String userName, Task task) {
        int taskId = getMaxTaskId()+1;

        jdbcTemplate.update("INSERT INTO tasks VALUES(?, ?, ?)", taskId,
                task.getTitle(), task.getDescription());
        List<User> userList= jdbcTemplate.query("SELECT userid FROM users WHERE username = ?",
                new BeanPropertyRowMapper<>(User.class), userName);
        if (userList.isEmpty()){
            System.out.println(USER_NOT_FOUND);
            return;
        }

        int userId = userList.stream().findAny().get().getUserId();

        jdbcTemplate.update("INSERT INTO user_task VALUES (?, ?)", userId, taskId);
        System.out.format(TASK_ASSIGNED, userName);

    }

    @Override
    public List<Task> getUserTasks(String userName){


        int userId = jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                new BeanPropertyRowMapper<>(User.class), userName).stream().findAny().get().getUserId();

        List<Task> result = jdbcTemplate.query("SELECT tasks.tasktitle AS title, " +
                        "tasks.taskdescription AS description " +
                        "FROM user_task " +
                        "JOIN tasks " +
                        "ON user_task.taskid = tasks.taskid " +
                        "WHERE userid = ?",
                new BeanPropertyRowMapper<>(Task.class), userId);
        if (result.isEmpty()){
            System.out.format(NO_TASKS_FOUND, userName);
        }
        return result;
    }
}
