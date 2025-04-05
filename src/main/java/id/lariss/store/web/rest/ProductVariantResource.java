package id.lariss.store.web.rest;

import id.lariss.store.repository.ProductVariantRepository;
import id.lariss.store.service.ProductVariantService;
import id.lariss.store.service.dto.ProductVariantDTO;
import id.lariss.store.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link id.lariss.store.domain.ProductVariant}.
 */
@RestController
@RequestMapping("/api/product-variants")
public class ProductVariantResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductVariantResource.class);

    private static final String ENTITY_NAME = "productVariant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductVariantService productVariantService;

    private final ProductVariantRepository productVariantRepository;

    public ProductVariantResource(ProductVariantService productVariantService, ProductVariantRepository productVariantRepository) {
        this.productVariantService = productVariantService;
        this.productVariantRepository = productVariantRepository;
    }

    /**
     * {@code POST  /product-variants} : Create a new productVariant.
     *
     * @param productVariantDTO the productVariantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productVariantDTO, or with status {@code 400 (Bad Request)} if the productVariant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductVariantDTO> createProductVariant(@Valid @RequestBody ProductVariantDTO productVariantDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductVariant : {}", productVariantDTO);
        if (productVariantDTO.getId() != null) {
            throw new BadRequestAlertException("A new productVariant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productVariantDTO = productVariantService.save(productVariantDTO);
        return ResponseEntity.created(new URI("/api/product-variants/" + productVariantDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productVariantDTO.getId().toString()))
            .body(productVariantDTO);
    }

    /**
     * {@code PUT  /product-variants/:id} : Updates an existing productVariant.
     *
     * @param id the id of the productVariantDTO to save.
     * @param productVariantDTO the productVariantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productVariantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductVariantDTO> updateProductVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductVariantDTO productVariantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductVariant : {}, {}", id, productVariantDTO);
        if (productVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productVariantDTO = productVariantService.update(productVariantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantDTO.getId().toString()))
            .body(productVariantDTO);
    }

    /**
     * {@code PATCH  /product-variants/:id} : Partial updates given fields of an existing productVariant, field will ignore if it is null
     *
     * @param id the id of the productVariantDTO to save.
     * @param productVariantDTO the productVariantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productVariantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productVariantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductVariantDTO> partialUpdateProductVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductVariantDTO productVariantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductVariant partially : {}, {}", id, productVariantDTO);
        if (productVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductVariantDTO> result = productVariantService.partialUpdate(productVariantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-variants} : get all the productVariants.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productVariants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductVariantDTO>> getAllProductVariants(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("cartitem-is-null".equals(filter)) {
            LOG.debug("REST request to get all ProductVariants where cartItem is null");
            return new ResponseEntity<>(productVariantService.findAllWhereCartItemIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of ProductVariants");
        Page<ProductVariantDTO> page;
        if (eagerload) {
            page = productVariantService.findAllWithEagerRelationships(pageable);
        } else {
            page = productVariantService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-variants/:id} : get the "id" productVariant.
     *
     * @param id the id of the productVariantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productVariantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantDTO> getProductVariant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductVariant : {}", id);
        Optional<ProductVariantDTO> productVariantDTO = productVariantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productVariantDTO);
    }

    /**
     * {@code DELETE  /product-variants/:id} : delete the "id" productVariant.
     *
     * @param id the id of the productVariantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductVariant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductVariant : {}", id);
        productVariantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
