package com.example.device_price_api.controller;

import com.example.device_price_api.exception.DeviceNotFoundException;
import com.example.device_price_api.model.Device;
import com.example.device_price_api.service.DeviceService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.findAllDevices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        try {
            Device device = deviceService.findDeviceById(id)
                    .orElseThrow(() -> new DeviceNotFoundException("Device not found with ID " + id));
            return ResponseEntity.ok(device);
        } catch (DeviceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> addDevice(@RequestBody Device device) {
        try {
            Device savedDevice = deviceService.saveDevice(device);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDevice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDuplicateKeyException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @GetMapping("/predict/{deviceId}")
    public ResponseEntity<Device> predictPriceForDevice(@PathVariable Long deviceId) {
        try {
            Device deviceWithPrediction = deviceService.predictPriceForDevice(deviceId);
            return ResponseEntity.ok(deviceWithPrediction);
        } catch (DeviceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
