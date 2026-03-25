package goltsman.btrestaurantservice.service.impl;


import goltsman.btrestaurantservice.exception.ResourceAlreadyExistsException;
import goltsman.btrestaurantservice.mapper.CuisineMapper;
import goltsman.btrestaurantservice.model.Cuisine;
import goltsman.btrestaurantservice.model.request.CreateCuisineRequest;
import goltsman.btrestaurantservice.model.request.UpdateCuisineRequest;
import goltsman.btrestaurantservice.model.response.CuisineResponse;
import goltsman.btrestaurantservice.model.response.MessageResponse;
import goltsman.btrestaurantservice.repository.CuisineRepository;
import goltsman.btrestaurantservice.service.CuisineService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;
    private final CuisineMapper cuisineMapper;

    @Override
    @Transactional
    public CuisineResponse create(CreateCuisineRequest request) {
        if (cuisineRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Кухня с таким названием уже существует");
        }
        Cuisine cuisine = cuisineMapper.mapCreateCuisineRequestToCuisine(request);
        cuisineRepository.save(cuisine);
        return cuisineMapper.mapCuisineToCuisineResponse(cuisine);
    }

    @Override
    @Transactional
    public CuisineResponse update(Long id, UpdateCuisineRequest request) {
        Cuisine cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кухня с id " + id + " не найдена"));
        cuisineMapper.mapUpdateCuisineRequestToCuisine(request, cuisine);
        cuisineRepository.save(cuisine);
        return cuisineMapper.mapCuisineToCuisineResponse(cuisine);
    }

    @Override
    @Transactional
    public MessageResponse delete(Long id) {
        Cuisine cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кухня с id " + id + " не найдена"));
        cuisineRepository.delete(cuisine);
        return MessageResponse.builder().message("Кухня успешно удалена").build();
    }

    @Override
    @Transactional(readOnly = true)
    public CuisineResponse getCuisine(Long id) {
        Cuisine cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кухня с id " + id + " не найдена"));
        return cuisineMapper.mapCuisineToCuisineResponse(cuisine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuisineResponse> getCuisines() {
        return cuisineRepository.findAll().stream().map(cuisineMapper::mapCuisineToCuisineResponse).toList();
    }
}