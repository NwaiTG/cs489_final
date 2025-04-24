package com.nwai.dentalsys.controller;

import com.nwai.dentalsys.dto.request.AddressRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    // 3. POST new address
    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(@RequestBody AddressRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.createAddress(requestDto));
    }

    // 4. PUT update address
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDto> updateAddress(@PathVariable int id, @RequestBody AddressRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updateAddress(id, requestDto));
    }

    // 5. DELETE address
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable int id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddressesSortedByCity());
    }
}
