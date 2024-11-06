package com.example.device_price_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DevicePredictionRequest {
    private Integer battery_power; // Total energy a battery can store in mAh
    private Integer blue;          // Has Bluetooth or not
    private Double clock_speed;    // Speed of the microprocessor
    private Integer dual_sim;      // Has dual SIM support
    private Double fc;             // Front Camera megapixels
    private Double four_g;         // Has 4G or not
    private Double int_memory;     // Internal Memory in GB
    private Double m_dep;          // Mobile Depth in cm
    private Double mobile_wt;      // Weight of mobile phone
    private Double n_cores;        // Number of cores of the processor
    private Double pc;             // Primary Camera megapixels
    private Double px_height;     // Pixel Resolution Height
    private Double px_width;      // Pixel Resolution Width
    private Double ram;           // RAM in MB
    private Double sc_h;           // Screen Height of mobile in cm
    private Double sc_w;           // Screen Width of mobile in cm
    private Integer talk_time;     // Longest time a single battery charge lasts
    private Integer three_g;       // Has 3G or not
    private Integer touch_screen;  // Has touch screen or not
    private Integer wifi;          // Has wifi or not
}
