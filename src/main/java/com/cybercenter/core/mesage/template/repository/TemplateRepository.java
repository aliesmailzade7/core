package com.cybercenter.core.mesage.template.repository;

import com.cybercenter.core.mesage.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Template findByName(String name);
}
