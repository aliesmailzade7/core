package com.cybercenter.core.mesage.template.service;

import com.cybercenter.core.mesage.template.entity.Template;
import com.cybercenter.core.mesage.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public Template findByName(String name){
        return templateRepository.findByName(name);
    }
}
