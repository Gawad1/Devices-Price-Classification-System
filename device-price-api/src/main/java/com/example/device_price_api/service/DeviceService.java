package com.example.device_price_api.service;

import com.example.device_price_api.dto.DevicePredictionRequest;
import com.example.device_price_api.exception.DeviceNotFoundException;
import com.example.device_price_api.exception.PredictionException;
import com.example.device_price_api.model.Device;
import com.example.device_price_api.repository.DeviceRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Value;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceService {

    @Value("${python.api.url}")
    private String pythonApiUrl;
    
    private final DeviceRepository deviceRepository;
    private final RestTemplate restTemplate;
    private ModelMapper modelMapper;

    public DeviceService(DeviceRepository deviceRepository, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.deviceRepository = deviceRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    public List<Device> findAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> findDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    public Device saveDevice(Device device) {
        if (device.getId() == null) {
            throw new IllegalArgumentException("Device ID is required.");
        }
        if (deviceRepository.existsById(device.getId())) {
            throw new DataIntegrityViolationException("Device with ID " + device.getId() + " already exists.");
        }
        device.setPredictedPrice(null);
        return deviceRepository.save(device);
    }

    @SuppressWarnings("null")
    @Transactional
    public Device predictPriceForDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with ID " + deviceId));

        // Map Device fields to DevicePredictionRequest fields using ModelMapper
        DevicePredictionRequest predictionRequest = modelMapper.map(device, DevicePredictionRequest.class);

        // Call Python API with the predictionRequest
        Integer predictedPrice;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Integer> response = restTemplate.postForObject(pythonApiUrl, predictionRequest, Map.class);
            predictedPrice = response.get("predicted_price");
            if (predictedPrice == null) {
                throw new PredictionException("Predicted price not found in the response.");
            }
        } catch (Exception e) {
            throw new PredictionException("Error occurred while predicting price: " + e.getMessage());
        }

        device.setPredictedPrice(predictedPrice);
        return deviceRepository.save(device);
    }
}
