package com.cx.kpi2.dao;

import com.cx.kpi2.pojo.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDAO extends JpaRepository<Account , String> {

    Account getByAccount(String account);

}
