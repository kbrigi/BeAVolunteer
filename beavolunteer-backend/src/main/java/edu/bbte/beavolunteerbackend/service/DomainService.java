package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.mapper.DomainMapper;
import edu.bbte.beavolunteerbackend.model.Domain;
import edu.bbte.beavolunteerbackend.repository.DomainRepository;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;

@Service
@Slf4j
public class DomainService extends ImgService {

    @Autowired
    private DomainRepository domainRepository;

//    @Autowired
//    private DomainMapper domainMapper;

    public void saveDomain(DomainDTO domainDTO, Blob img) throws BusinessException {
        if(domainRepository.checkName(domainDTO.getDomain_name()) == 0) {
            Domain domain = DomainMapper.domainDTOToDomain(domainDTO);
            domain.setDomainImg(img);
            log.info(String.valueOf(domain));
            domainRepository.saveAndFlush(domain);
        } else {
            throw new BusinessException("Domain name must be unique");
        }
    }

    public Collection<DomainDTO> getAll() {
        return DomainMapper.domainsToDTO(domainRepository.findAll());
    }

    public void remove(Long id) {
        domainRepository.delete(domainRepository.getById(id));
    }

    public byte[] getImage(Long id) throws SQLException {
        Blob domainImg = domainRepository.getById(id).getDomainImg();
        return getImg(domainImg);
    }
}