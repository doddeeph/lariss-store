import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './product-variant.reducer';

export const ProductVariant = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const productVariantList = useAppSelector(state => state.productVariant.entities);
  const loading = useAppSelector(state => state.productVariant.loading);
  const totalItems = useAppSelector(state => state.productVariant.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="product-variant-heading" data-cy="ProductVariantHeading">
        <Translate contentKey="larissStoreApp.productVariant.home.title">Product Variants</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="larissStoreApp.productVariant.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-variant/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="larissStoreApp.productVariant.home.createLabel">Create new Product Variant</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productVariantList && productVariantList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="larissStoreApp.productVariant.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('imageUrl')}>
                  <Translate contentKey="larissStoreApp.productVariant.imageUrl">Image Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('imageUrl')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="larissStoreApp.productVariant.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('color')}>
                  <Translate contentKey="larissStoreApp.productVariant.color">Color</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('color')} />
                </th>
                <th className="hand" onClick={sort('processor')}>
                  <Translate contentKey="larissStoreApp.productVariant.processor">Processor</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processor')} />
                </th>
                <th className="hand" onClick={sort('memory')}>
                  <Translate contentKey="larissStoreApp.productVariant.memory">Memory</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('memory')} />
                </th>
                <th className="hand" onClick={sort('storage')}>
                  <Translate contentKey="larissStoreApp.productVariant.storage">Storage</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storage')} />
                </th>
                <th className="hand" onClick={sort('screen')}>
                  <Translate contentKey="larissStoreApp.productVariant.screen">Screen</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('screen')} />
                </th>
                <th className="hand" onClick={sort('connectivity')}>
                  <Translate contentKey="larissStoreApp.productVariant.connectivity">Connectivity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('connectivity')} />
                </th>
                <th className="hand" onClick={sort('material')}>
                  <Translate contentKey="larissStoreApp.productVariant.material">Material</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('material')} />
                </th>
                <th className="hand" onClick={sort('caseSize')}>
                  <Translate contentKey="larissStoreApp.productVariant.caseSize">Case Size</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('caseSize')} />
                </th>
                <th className="hand" onClick={sort('strapColor')}>
                  <Translate contentKey="larissStoreApp.productVariant.strapColor">Strap Color</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('strapColor')} />
                </th>
                <th className="hand" onClick={sort('strapSize')}>
                  <Translate contentKey="larissStoreApp.productVariant.strapSize">Strap Size</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('strapSize')} />
                </th>
                <th className="hand" onClick={sort('summary')}>
                  <Translate contentKey="larissStoreApp.productVariant.summary">Summary</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('summary')} />
                </th>
                <th>
                  <Translate contentKey="larissStoreApp.productVariant.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productVariantList.map((productVariant, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-variant/${productVariant.id}`} color="link" size="sm">
                      {productVariant.id}
                    </Button>
                  </td>
                  <td>{productVariant.imageUrl}</td>
                  <td>{productVariant.price}</td>
                  <td>{productVariant.color}</td>
                  <td>{productVariant.processor}</td>
                  <td>{productVariant.memory}</td>
                  <td>{productVariant.storage}</td>
                  <td>{productVariant.screen}</td>
                  <td>{productVariant.connectivity}</td>
                  <td>{productVariant.material}</td>
                  <td>{productVariant.caseSize}</td>
                  <td>{productVariant.strapColor}</td>
                  <td>{productVariant.strapSize}</td>
                  <td>{productVariant.summary}</td>
                  <td>
                    {productVariant.product ? (
                      <Link to={`/product/${productVariant.product.id}`}>{productVariant.product.productName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product-variant/${productVariant.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-variant/${productVariant.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/product-variant/${productVariant.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="larissStoreApp.productVariant.home.notFound">No Product Variants found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={productVariantList && productVariantList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default ProductVariant;
