package id.lariss.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductVariant.
 */
@Entity
@Table(name = "product_variant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "color")
    private String color;

    @Column(name = "processor")
    private String processor;

    @Column(name = "memory")
    private String memory;

    @Column(name = "storage")
    private String storage;

    @Column(name = "screen")
    private String screen;

    @Column(name = "connectivity")
    private String connectivity;

    @Column(name = "material")
    private String material;

    @Column(name = "case_size")
    private String caseSize;

    @Column(name = "strap_color")
    private String strapColor;

    @Column(name = "strap_size")
    private String strapSize;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productVariant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order", "productVariant" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @JsonIgnoreProperties(value = { "productVariant", "cart" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "productVariant")
    private CartItem cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productVariants", "category" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductVariant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public ProductVariant imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public ProductVariant price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getColor() {
        return this.color;
    }

    public ProductVariant color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProcessor() {
        return this.processor;
    }

    public ProductVariant processor(String processor) {
        this.setProcessor(processor);
        return this;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getMemory() {
        return this.memory;
    }

    public ProductVariant memory(String memory) {
        this.setMemory(memory);
        return this;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getStorage() {
        return this.storage;
    }

    public ProductVariant storage(String storage) {
        this.setStorage(storage);
        return this;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getScreen() {
        return this.screen;
    }

    public ProductVariant screen(String screen) {
        this.setScreen(screen);
        return this;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getConnectivity() {
        return this.connectivity;
    }

    public ProductVariant connectivity(String connectivity) {
        this.setConnectivity(connectivity);
        return this;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public String getMaterial() {
        return this.material;
    }

    public ProductVariant material(String material) {
        this.setMaterial(material);
        return this;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCaseSize() {
        return this.caseSize;
    }

    public ProductVariant caseSize(String caseSize) {
        this.setCaseSize(caseSize);
        return this;
    }

    public void setCaseSize(String caseSize) {
        this.caseSize = caseSize;
    }

    public String getStrapColor() {
        return this.strapColor;
    }

    public ProductVariant strapColor(String strapColor) {
        this.setStrapColor(strapColor);
        return this;
    }

    public void setStrapColor(String strapColor) {
        this.strapColor = strapColor;
    }

    public String getStrapSize() {
        return this.strapSize;
    }

    public ProductVariant strapSize(String strapSize) {
        this.setStrapSize(strapSize);
        return this;
    }

    public void setStrapSize(String strapSize) {
        this.strapSize = strapSize;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setProductVariant(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setProductVariant(this));
        }
        this.orderItems = orderItems;
    }

    public ProductVariant orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public ProductVariant addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setProductVariant(this);
        return this;
    }

    public ProductVariant removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setProductVariant(null);
        return this;
    }

    public CartItem getCartItem() {
        return this.cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        if (this.cartItem != null) {
            this.cartItem.setProductVariant(null);
        }
        if (cartItem != null) {
            cartItem.setProductVariant(this);
        }
        this.cartItem = cartItem;
    }

    public ProductVariant cartItem(CartItem cartItem) {
        this.setCartItem(cartItem);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariant product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariant)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductVariant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariant{" +
            "id=" + getId() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", price=" + getPrice() +
            ", color='" + getColor() + "'" +
            ", processor='" + getProcessor() + "'" +
            ", memory='" + getMemory() + "'" +
            ", storage='" + getStorage() + "'" +
            ", screen='" + getScreen() + "'" +
            ", connectivity='" + getConnectivity() + "'" +
            ", material='" + getMaterial() + "'" +
            ", caseSize='" + getCaseSize() + "'" +
            ", strapColor='" + getStrapColor() + "'" +
            ", strapSize='" + getStrapSize() + "'" +
            "}";
    }
}
