import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { createEntity, getEntity, reset, updateEntity } from './product-variant.reducer';

export const ProductVariantUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const products = useAppSelector(state => state.product.entities);
  const productVariantEntity = useAppSelector(state => state.productVariant.entity);
  const loading = useAppSelector(state => state.productVariant.loading);
  const updating = useAppSelector(state => state.productVariant.updating);
  const updateSuccess = useAppSelector(state => state.productVariant.updateSuccess);

  const handleClose = () => {
    navigate(`/product-variant${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...productVariantEntity,
      ...values,
      product: products.find(it => it.id.toString() === values.product?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...productVariantEntity,
          product: productVariantEntity?.product?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissStoreApp.productVariant.home.createOrEditLabel" data-cy="ProductVariantCreateUpdateHeading">
            <Translate contentKey="larissStoreApp.productVariant.home.createOrEditLabel">Create or edit a ProductVariant</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="product-variant-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissStoreApp.productVariant.imageUrl')}
                id="product-variant-imageUrl"
                name="imageUrl"
                data-cy="imageUrl"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.price')}
                id="product-variant-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.color')}
                id="product-variant-color"
                name="color"
                data-cy="color"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.processor')}
                id="product-variant-processor"
                name="processor"
                data-cy="processor"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.memory')}
                id="product-variant-memory"
                name="memory"
                data-cy="memory"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.storage')}
                id="product-variant-storage"
                name="storage"
                data-cy="storage"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.screen')}
                id="product-variant-screen"
                name="screen"
                data-cy="screen"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.connectivity')}
                id="product-variant-connectivity"
                name="connectivity"
                data-cy="connectivity"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.material')}
                id="product-variant-material"
                name="material"
                data-cy="material"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.caseSize')}
                id="product-variant-caseSize"
                name="caseSize"
                data-cy="caseSize"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.strapColor')}
                id="product-variant-strapColor"
                name="strapColor"
                data-cy="strapColor"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.strapSize')}
                id="product-variant-strapSize"
                name="strapSize"
                data-cy="strapSize"
                type="text"
              />
              <ValidatedField
                label={translate('larissStoreApp.productVariant.summary')}
                id="product-variant-summary"
                name="summary"
                data-cy="summary"
                type="text"
              />
              <ValidatedField
                id="product-variant-product"
                name="product"
                data-cy="product"
                label={translate('larissStoreApp.productVariant.product')}
                type="select"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.productName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-variant" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProductVariantUpdate;
