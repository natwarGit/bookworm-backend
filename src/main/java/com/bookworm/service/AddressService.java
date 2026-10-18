package com.bookworm.service;

import com.bookworm.dto.AddressRequest;
import com.bookworm.dto.AddressResponse;
import com.bookworm.entity.Address;
import com.bookworm.entity.User;
import com.bookworm.exception.ResourceNotFoundException;
import com.bookworm.repository.AddressRepository;
import com.bookworm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<AddressResponse> getAddressesForUser(UUID userId) {
        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse addAddress(AddressRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        Address address = new Address();
        address.setUser(user);
        address.setLabel(request.label());
        address.setFullName(request.fullName());
        address.setAddressLine1(request.addressLine1());
        address.setAddressLine2(request.addressLine2());
        address.setCity(request.city());
        address.setState(request.state());
        address.setPincode(request.pincode());
        address.setPhone(request.phone());

        return mapToResponse(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(UUID addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new ResourceNotFoundException("Address not found: " + addressId);
        }
        addressRepository.deleteById(addressId);
    }

    private AddressResponse mapToResponse(Address a) {
        return new AddressResponse(
                a.getId(), a.getLabel(), a.getFullName(),
                a.getAddressLine1(), a.getAddressLine2(),
                a.getCity(), a.getState(), a.getPincode(), a.getPhone()
        );
    }
}
