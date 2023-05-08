package com.sybercenter.core.mesageSender.template.repository;

import com.sybercenter.core.mesageSender.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Template findByName(String name);
}
