package goltsman.btrestaurantservice.service;

import goltsman.btrestaurantservice.model.request.CreateCuisineRequest;
import goltsman.btrestaurantservice.model.request.UpdateCuisineRequest;
import goltsman.btrestaurantservice.model.response.CuisineResponse;
import goltsman.btrestaurantservice.model.response.MessageResponse;

import java.util.List;

public interface CuisineService {

    CuisineResponse create(CreateCuisineRequest request);

    CuisineResponse update(Long id, UpdateCuisineRequest request);

    MessageResponse delete(Long id);

    CuisineResponse getCuisine(Long id);

    List<CuisineResponse> getCuisines();

}