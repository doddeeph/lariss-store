import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-variant.reducer';

export const ProductVariantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productVariantEntity = useAppSelector(state => state.productVariant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productVariantDetailsHeading">
          <Translate contentKey="larissStoreApp.productVariant.detail.title">ProductVariant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.id}</dd>
          <dt>
            <span id="imageUrl">
              <Translate contentKey="larissStoreApp.productVariant.imageUrl">Image Url</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.imageUrl}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="larissStoreApp.productVariant.price">Price</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.price}</dd>
          <dt>
            <span id="color">
              <Translate contentKey="larissStoreApp.productVariant.color">Color</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.color}</dd>
          <dt>
            <span id="processor">
              <Translate contentKey="larissStoreApp.productVariant.processor">Processor</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.processor}</dd>
          <dt>
            <span id="memory">
              <Translate contentKey="larissStoreApp.productVariant.memory">Memory</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.memory}</dd>
          <dt>
            <span id="storage">
              <Translate contentKey="larissStoreApp.productVariant.storage">Storage</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.storage}</dd>
          <dt>
            <span id="screen">
              <Translate contentKey="larissStoreApp.productVariant.screen">Screen</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.screen}</dd>
          <dt>
            <span id="connectivity">
              <Translate contentKey="larissStoreApp.productVariant.connectivity">Connectivity</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.connectivity}</dd>
          <dt>
            <span id="material">
              <Translate contentKey="larissStoreApp.productVariant.material">Material</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.material}</dd>
          <dt>
            <span id="caseSize">
              <Translate contentKey="larissStoreApp.productVariant.caseSize">Case Size</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.caseSize}</dd>
          <dt>
            <span id="strapColor">
              <Translate contentKey="larissStoreApp.productVariant.strapColor">Strap Color</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.strapColor}</dd>
          <dt>
            <span id="strapSize">
              <Translate contentKey="larissStoreApp.productVariant.strapSize">Strap Size</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.strapSize}</dd>
          <dt>
            <span id="summary">
              <Translate contentKey="larissStoreApp.productVariant.summary">Summary</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.summary}</dd>
          <dt>
            <Translate contentKey="larissStoreApp.productVariant.product">Product</Translate>
          </dt>
          <dd>{productVariantEntity.product ? productVariantEntity.product.productName : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-variant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-variant/${productVariantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductVariantDetail;
