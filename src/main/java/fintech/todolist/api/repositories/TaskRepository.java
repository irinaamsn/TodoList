package fintech.todolist.api.repositories;

import fintech.todolist.api.models.AppUser;
import fintech.todolist.api.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    boolean existsById(UUID id);

    List<Task> findAllByUser(AppUser user);

    @Modifying
    void deleteById(UUID id);

    @Modifying
    @Query("update Task set title= :title, details= :details, deadline= :deadline where id= :id")
    void updateById(@Param("id") UUID id, @Param("title") String title,
                    @Param("details") String details,
                    @Param("deadline") LocalDateTime deadline);

    @Modifying
    @Query("update Task set isCompleted= :isCompleted, dateTimeLastChange=current_timestamp where id= :id")
    void updateComplete(@Param("id") UUID id, @Param("isCompleted") boolean isCompleted);

}
