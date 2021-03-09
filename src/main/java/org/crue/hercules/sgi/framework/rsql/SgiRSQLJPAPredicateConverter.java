package org.crue.hercules.sgi.framework.rsql;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import io.github.perplexhub.rsql.RSQLJPAPredicateConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * Converter to build predicates from a RSQL query
 */
@Slf4j
public class SgiRSQLJPAPredicateConverter extends RSQLJPAPredicateConverter {

  private final SgiRSQLPredicateResolver<?> specificationResolver;
  protected CriteriaBuilder builder;
  CriteriaQuery<?> query;

  /**
   * @param builder           {@link CriteriaBuilder} to use in predicate
   *                          generation
   * @param query             {@link CriteriaQuery} to use in predicate generation
   * @param predicateResolver {@link SgiRSQLPredicateResolver} to use for custom
   *                          properties not present in the entity
   */
  public SgiRSQLJPAPredicateConverter(CriteriaBuilder builder, CriteriaQuery<?> query,
      SgiRSQLPredicateResolver<?> predicateResolver) {
    super(builder, null, null);
    log.debug(
        "SgiRSQLJPAPredicateConverter(CriteriaBuilder builder, CriteriaQuery<?> query, SgiRSQLPredicateResolver<?> predicateResolver) - start");
    this.builder = builder;
    this.query = query;
    this.specificationResolver = predicateResolver;
    log.debug(
        "SgiRSQLJPAPredicateConverter(CriteriaBuilder builder, CriteriaQuery<?> query, SgiRSQLPredicateResolver<?> predicateResolver) - end");
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Predicate visit(ComparisonNode node, From root) {
    log.debug("visit(ComparisonNode node, From root) - start");
    Predicate returnValue;
    if (this.specificationResolver != null && this.specificationResolver.isManaged(node)) {
      returnValue = specificationResolver.toPredicate(node, (Root) root, this.query, this.builder);
    } else {
      returnValue = super.visit(node, root);
    }
    log.debug("visit(ComparisonNode node, From root) - end");
    return returnValue;
  }
}
