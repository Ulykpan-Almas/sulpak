package com.example.firstwork.repositories;

import com.example.firstwork.entites.Comments;;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

    List<Comments> findAllByItemIdOrderByAddedDateDesc(Long item_id);

}
