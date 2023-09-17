package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//CRUD 함수를 JPARepository가 들고 있음.
// @Repository라는 어노테이션이 없어도 DI가 됨. 이유는 JpaRepository를 상속했기 때문에.
public interface UserRepository extends JpaRepository<User, Integer> {
    //findBy규칙
    //select * from user where username=1?
    public User findByUsername(String username); //Jpa @Query 메소드들을 공부하면 더 자세한 내용 나옴.
}
