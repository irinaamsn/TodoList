package fintech.todolist.api.repositories;

import fintech.todolist.api.models.AppUser;
import fintech.todolist.api.models.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TodolistRepository extends JpaRepository<Todolist, UUID> {
    boolean existsById(UUID id);
    List<Todolist> findAllByUser(AppUser user);

    @Modifying
    void deleteById(UUID id);

    @Modifying
    @Query("update Todolist set title= :title where id= :id")
    void updateTodolistById(@Param("id")UUID id, @Param("title")String title);
}
