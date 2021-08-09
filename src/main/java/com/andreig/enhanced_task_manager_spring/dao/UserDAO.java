package com.andreig.enhanced_task_manager_spring.dao;

import com.andreig.enhanced_task_manager_spring.model.Task;
import com.andreig.enhanced_task_manager_spring.model.TaskInfo;
import com.andreig.enhanced_task_manager_spring.model.User;
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
        jdbcTemplate.update("INSERT INTO users VALUES(?, ?, ?)",
                user.getFirstName(), user.getLastName(), user.getUserName());
        System.out.println(USER_ADDED);
    }

    public void addTeamId(String userName, int id){
        jdbcTemplate.update("UPDATE users SET teamId=? WHERE userName=?", id, userName);
    }

    public void assignTaskToTeam(int teamId, Task task){

    }

    @Override
    public List<User> showAllUsers(){
        return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
    }

    public int getMaxTaskId(){
        return jdbcTemplate.query("SELECT taskid FROM tasks", new BeanPropertyRowMapper<>(Task.class)).
                stream().max(Comparator.comparingInt(Task::getTaskId)).get().getTaskId();
    }

    @Override
    public void assignTask(String userName, Task task) {
        jdbcTemplate.query("SELECT taskid FROM tasks WHERE userName=?", new BeanPropertyRowMapper<>(String.class),
                userName);
        String taskInfo = "Title: " + task.getTitle() + " /Description: " + task.getDescription() + "// ";
    }

    public List<String> gets(String userName){
        return jdbcTemplate.query("SELECT tasks FROM users WHERE userName = ?",
                new BeanPropertyRowMapper<>(String.class), userName);
    }

    @Override
    public List<Task> getUserTasks(String userName){
        List<Task> result = new ArrayList<>();

        ArrayList<TaskInfo> s = (ArrayList<TaskInfo>) jdbcTemplate.query("SELECT taskid FROM users WHERE userName = ?",
                new BeanPropertyRowMapper<>(TaskInfo.class), userName);

        System.out.println(s.get(0).getInfo());

        return result;
    }
}
