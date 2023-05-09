package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.mapper.DomainMapper;
import edu.bbte.beavolunteerbackend.model.*;
import edu.bbte.beavolunteerbackend.model.repository.DomainRepository;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DomainService extends ImgService {

    @Autowired
    private DomainRepository domainRepository;

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
        return DomainMapper.domainsToDTO(domainRepository.findAllAsc());
    }

    public void remove(Long id) {
        domainRepository.delete(domainRepository.getById(id));
    }

    public byte[] getImage(String name) throws SQLException {
        Blob domainImg = domainRepository.findByName(name).getDomainImg();
        return getImg(domainImg);
    }

    public List<DomainDTO> getDomains(List <Long> domain_ids) {
        List <DomainDTO> domainDTOS = new ArrayList<>();
        for (Long did : domain_ids) {
            Optional<Domain> domain = domainRepository.findById(did);
            if (domain.isPresent()) {
                DomainDTO domainDTO = DomainMapper.domainToDTO(domain.get());
                domainDTOS.add(domainDTO);
            }
        }
        return domainDTOS;
    }
}
