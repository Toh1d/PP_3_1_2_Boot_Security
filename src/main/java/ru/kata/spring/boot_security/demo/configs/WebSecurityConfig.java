package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.services.UserServiceImp;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserServiceImp userService; //инжектим UserServiceImp и передаем его в метод daoAuthenticationProvider
    //TODO: возможно тут надо UserDetailsService для циклички
    private final SuccessUserHandler successUserHandler;
//    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, @Lazy UserServiceImp userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                //используется для отправки юзеров с разными ролями на разные страницы
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    //тут по видео пишу 3 вид аутентификации: DAOAuthenticationProvider
    //1. Создать модели
    //2. Создать interface UserRepository extends JPARepository<User, Long>
    //3. тоже самое для RoleRepository
    //теперь настройка аутентификации самой
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        //AuthenticationProvider'у мы отдаем логин и пароль. Он проверяет наличие такого пользователя
        //в бд. Если не существует, должны положить в SpringSecurityContext;
        //Он понимает о существовании с помощью метода setUserDetailsService().
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }


    //Для преобразования пароля из строкового представления в bcrypt hash
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // аутентификация inMemory
    //Мой коммент: такой тип аутентификации сохр в памяти прил. Без бд. Не сохр при перезапуске прил.
    //Мой коммент: для сохранения своих пользователей
    //UserDetails - это мин инф о пользователе. Включает имя, пароль, роль
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user = //Создаем нового юзера
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//        //Тут можно также другого юзера создать
//        return new InMemoryUserDetailsManager(user); //загоняем пользователей в память
//    }

    //Тут по видео пишу второй вид аутентификации: jdbcAuthentication
    //Аутентификация через бд, но со стандартными таблицами, которыми мы не можем особо управлять
//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource) {
//        //Как положить юзеров в бд, описывается ниже
//        UserDetails user =
//                User.withUserDetails()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("admin")
//                        .roles("USER", "ADMIN")
//                        .build();
//
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource); //обяз
//        if (jdbcUserDetailsManager.userExists(user.getUsername())) {
//            jdbcUserDetailsManager.deleteUser(user.getUsername()); //проверка на сущ такого юзера в бд
//        }
//        if (jdbcUserDetailsManager.userExists(admin.getUsername())) {
//            jdbcUserDetailsManager.deleteUser(admin.getUsername());
//        }
//        jdbcUserDetailsManager.createUser(user); //эта строка и ниже создаст юзеров в бд
//        jdbcUserDetailsManager.createUser(admin); //Но можно вручную в бд добавить
//        return jdbcUserDetailsManager; //обяз
//        //в таком способе обязательны: 2 строки
//    }
}