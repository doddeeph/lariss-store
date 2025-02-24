package id.lariss.store.service.impl;

import id.lariss.store.domain.Customer;
import id.lariss.store.repository.CustomerRepository;
import id.lariss.store.service.CustomerService;
import id.lariss.store.service.dto.CustomerDTO;
import id.lariss.store.service.mapper.CustomerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.store.domain.Customer}.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        LOG.debug("Request to save Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        LOG.debug("Request to update Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Override
    public Optional<CustomerDTO> partialUpdate(CustomerDTO customerDTO) {
        LOG.debug("Request to partially update Customer : {}", customerDTO);

        return customerRepository
            .findById(customerDTO.getId())
            .map(existingCustomer -> {
                customerMapper.partialUpdate(existingCustomer, customerDTO);

                return existingCustomer;
            })
            .map(customerRepository::save)
            .map(customerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Customers");
        return customerRepository.findAll(pageable).map(customerMapper::toDto);
    }

    /**
     *  Get all the customers where Cart is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> findAllWhereCartIsNull() {
        LOG.debug("Request to get all customers where Cart is null");
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
            .filter(customer -> customer.getCart() == null)
            .map(customerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findOne(Long id) {
        LOG.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<CustomerDTO> findOneByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber).map(customerMapper::toDto);
    }

    @Override
    public CustomerDTO upsertByPhoneNumber(CustomerDTO customerDTO) {
        Customer customer = customerRepository
            .findByPhoneNumber(customerDTO.getPhoneNumber())
            .map(existingCustomer -> {
                customerMapper.partialUpdate(existingCustomer, customerDTO);
                return existingCustomer;
            })
            .orElse(customerMapper.toEntity(customerDTO));
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }
}
