package com.andreig.enhanced_task_manager_spring;

import com.andreig.enhanced_task_manager_spring.dao.UserDAO;
import com.andreig.enhanced_task_manager_spring.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EnhancedTaskManagerSpringApplication {

    public static void main(String[] args) {
        User user = new User ("Che", "Guevara","Comandantes");
        ApplicationContext ctx = SpringApplication.run(EnhancedTaskManagerSpringApplication.class, args);

        UserDAO userDAO = ctx.getBean("userDAO", UserDAO.class);
        userDAO.createUser(user);
        userDAO.showAllUsers().forEach(u -> System.out.println(u.toString()));
        System.out.println(userDAO.getMaxTaskId());
        userDAO.getUserTasks("Comandante");
    }

}
