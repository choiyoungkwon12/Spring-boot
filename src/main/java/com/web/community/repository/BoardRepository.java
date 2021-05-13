package com.web.community.repository;

import com.web.community.domain.Board;
import com.web.community.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
    Board findByUser(User user);
}
