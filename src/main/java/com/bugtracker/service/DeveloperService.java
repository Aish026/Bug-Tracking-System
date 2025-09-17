package com.bugtracker.service;

import com.bugtracker.dto.DeveloperDTO;
import com.bugtracker.model.Developer;
import com.bugtracker.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    private DeveloperDTO convertToDTO(Developer dev) {
        if (dev == null) return null;
        DeveloperDTO dto = new DeveloperDTO();
        dto.setId(dev.getId());
        dto.setUsername(dev.getUsername());
        dto.setEmail(dev.getEmail());
        return dto;
    }

    private Developer convertToEntity(DeveloperDTO dto) {
        if (dto == null) return null;
        Developer dev = new Developer();
        dev.setId(dto.getId());
        dev.setUsername(dto.getUsername());
        dev.setEmail(dto.getEmail());
        return dev;
    }

    public List<DeveloperDTO> getAllDevelopers() {
        return developerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DeveloperDTO getDeveloperById(Long id) {
        Optional<Developer> dev = developerRepository.findById(id);
        return dev.map(this::convertToDTO).orElse(null);
    }

    public DeveloperDTO createDeveloper(DeveloperDTO dto) {
        Developer saved = developerRepository.save(convertToEntity(dto));
        return convertToDTO(saved);
    }

    public DeveloperDTO updateDeveloper(Long id, DeveloperDTO dto) {
        return developerRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(dto.getUsername());
                    existing.setEmail(dto.getEmail());
                    Developer updated = developerRepository.save(existing);
                    return convertToDTO(updated);
                })
                .orElse(null);
    }

    public void deleteDeveloper(Long id) {
        developerRepository.deleteById(id);
    }
}
