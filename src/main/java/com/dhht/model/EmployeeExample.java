package com.dhht.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public EmployeeExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andEmployeeCodeIsNull() {
            addCriterion("employee_code is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeIsNotNull() {
            addCriterion("employee_code is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeEqualTo(String value) {
            addCriterion("employee_code =", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeNotEqualTo(String value) {
            addCriterion("employee_code <>", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeGreaterThan(String value) {
            addCriterion("employee_code >", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("employee_code >=", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeLessThan(String value) {
            addCriterion("employee_code <", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeLessThanOrEqualTo(String value) {
            addCriterion("employee_code <=", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeLike(String value) {
            addCriterion("employee_code like", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeNotLike(String value) {
            addCriterion("employee_code not like", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeIn(List<String> values) {
            addCriterion("employee_code in", values, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeNotIn(List<String> values) {
            addCriterion("employee_code not in", values, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeBetween(String value1, String value2) {
            addCriterion("employee_code between", value1, value2, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeNotBetween(String value1, String value2) {
            addCriterion("employee_code not between", value1, value2, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameIsNull() {
            addCriterion("employee_name is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameIsNotNull() {
            addCriterion("employee_name is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameEqualTo(String value) {
            addCriterion("employee_name =", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameNotEqualTo(String value) {
            addCriterion("employee_name <>", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameGreaterThan(String value) {
            addCriterion("employee_name >", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameGreaterThanOrEqualTo(String value) {
            addCriterion("employee_name >=", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameLessThan(String value) {
            addCriterion("employee_name <", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameLessThanOrEqualTo(String value) {
            addCriterion("employee_name <=", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameLike(String value) {
            addCriterion("employee_name like", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameNotLike(String value) {
            addCriterion("employee_name not like", value, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameIn(List<String> values) {
            addCriterion("employee_name in", values, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameNotIn(List<String> values) {
            addCriterion("employee_name not in", values, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameBetween(String value1, String value2) {
            addCriterion("employee_name between", value1, value2, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeNameNotBetween(String value1, String value2) {
            addCriterion("employee_name not between", value1, value2, "employeeName");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdIsNull() {
            addCriterion("employee_id is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdIsNotNull() {
            addCriterion("employee_id is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdEqualTo(String value) {
            addCriterion("employee_id =", value, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdNotEqualTo(String value) {
            addCriterion("employee_id <>", value, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdGreaterThan(String value) {
            addCriterion("employee_id >", value, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdGreaterThanOrEqualTo(String value) {
            addCriterion("employee_id >=", value, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdLessThan(String value) {
            addCriterion("employee_id <", value, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdLessThanOrEqualTo(String value) {
            addCriterion("employee_id <=", value, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdLike(String value) {
            addCriterion("employee_id like", value, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdNotLike(String value) {
            addCriterion("employee_id not like", value, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdIn(List<String> values) {
            addCriterion("employee_id in", values, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdNotIn(List<String> values) {
            addCriterion("employee_id not in", values, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdBetween(String value1, String value2) {
            addCriterion("employee_id between", value1, value2, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeIdNotBetween(String value1, String value2) {
            addCriterion("employee_id not between", value1, value2, "employeeId");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobIsNull() {
            addCriterion("employee_job is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobIsNotNull() {
            addCriterion("employee_job is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobEqualTo(String value) {
            addCriterion("employee_job =", value, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobNotEqualTo(String value) {
            addCriterion("employee_job <>", value, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobGreaterThan(String value) {
            addCriterion("employee_job >", value, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobGreaterThanOrEqualTo(String value) {
            addCriterion("employee_job >=", value, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobLessThan(String value) {
            addCriterion("employee_job <", value, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobLessThanOrEqualTo(String value) {
            addCriterion("employee_job <=", value, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobLike(String value) {
            addCriterion("employee_job like", value, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobNotLike(String value) {
            addCriterion("employee_job not like", value, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobIn(List<String> values) {
            addCriterion("employee_job in", values, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobNotIn(List<String> values) {
            addCriterion("employee_job not in", values, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobBetween(String value1, String value2) {
            addCriterion("employee_job between", value1, value2, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeJobNotBetween(String value1, String value2) {
            addCriterion("employee_job not between", value1, value2, "employeeJob");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationIsNull() {
            addCriterion("employee_nation is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationIsNotNull() {
            addCriterion("employee_nation is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationEqualTo(String value) {
            addCriterion("employee_nation =", value, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationNotEqualTo(String value) {
            addCriterion("employee_nation <>", value, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationGreaterThan(String value) {
            addCriterion("employee_nation >", value, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationGreaterThanOrEqualTo(String value) {
            addCriterion("employee_nation >=", value, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationLessThan(String value) {
            addCriterion("employee_nation <", value, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationLessThanOrEqualTo(String value) {
            addCriterion("employee_nation <=", value, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationLike(String value) {
            addCriterion("employee_nation like", value, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationNotLike(String value) {
            addCriterion("employee_nation not like", value, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationIn(List<String> values) {
            addCriterion("employee_nation in", values, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationNotIn(List<String> values) {
            addCriterion("employee_nation not in", values, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationBetween(String value1, String value2) {
            addCriterion("employee_nation between", value1, value2, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andEmployeeNationNotBetween(String value1, String value2) {
            addCriterion("employee_nation not between", value1, value2, "employeeNation");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressIsNull() {
            addCriterion("family_address is null");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressIsNotNull() {
            addCriterion("family_address is not null");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressEqualTo(String value) {
            addCriterion("family_address =", value, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressNotEqualTo(String value) {
            addCriterion("family_address <>", value, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressGreaterThan(String value) {
            addCriterion("family_address >", value, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressGreaterThanOrEqualTo(String value) {
            addCriterion("family_address >=", value, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressLessThan(String value) {
            addCriterion("family_address <", value, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressLessThanOrEqualTo(String value) {
            addCriterion("family_address <=", value, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressLike(String value) {
            addCriterion("family_address like", value, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressNotLike(String value) {
            addCriterion("family_address not like", value, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressIn(List<String> values) {
            addCriterion("family_address in", values, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressNotIn(List<String> values) {
            addCriterion("family_address not in", values, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressBetween(String value1, String value2) {
            addCriterion("family_address between", value1, value2, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressNotBetween(String value1, String value2) {
            addCriterion("family_address not between", value1, value2, "familyAddress");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailIsNull() {
            addCriterion("family_address_detail is null");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailIsNotNull() {
            addCriterion("family_address_detail is not null");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailEqualTo(String value) {
            addCriterion("family_address_detail =", value, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailNotEqualTo(String value) {
            addCriterion("family_address_detail <>", value, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailGreaterThan(String value) {
            addCriterion("family_address_detail >", value, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailGreaterThanOrEqualTo(String value) {
            addCriterion("family_address_detail >=", value, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailLessThan(String value) {
            addCriterion("family_address_detail <", value, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailLessThanOrEqualTo(String value) {
            addCriterion("family_address_detail <=", value, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailLike(String value) {
            addCriterion("family_address_detail like", value, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailNotLike(String value) {
            addCriterion("family_address_detail not like", value, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailIn(List<String> values) {
            addCriterion("family_address_detail in", values, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailNotIn(List<String> values) {
            addCriterion("family_address_detail not in", values, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailBetween(String value1, String value2) {
            addCriterion("family_address_detail between", value1, value2, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andFamilyAddressDetailNotBetween(String value1, String value2) {
            addCriterion("family_address_detail not between", value1, value2, "familyAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressIsNull() {
            addCriterion("now_address is null");
            return (Criteria) this;
        }

        public Criteria andNowAddressIsNotNull() {
            addCriterion("now_address is not null");
            return (Criteria) this;
        }

        public Criteria andNowAddressEqualTo(String value) {
            addCriterion("now_address =", value, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressNotEqualTo(String value) {
            addCriterion("now_address <>", value, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressGreaterThan(String value) {
            addCriterion("now_address >", value, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressGreaterThanOrEqualTo(String value) {
            addCriterion("now_address >=", value, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressLessThan(String value) {
            addCriterion("now_address <", value, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressLessThanOrEqualTo(String value) {
            addCriterion("now_address <=", value, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressLike(String value) {
            addCriterion("now_address like", value, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressNotLike(String value) {
            addCriterion("now_address not like", value, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressIn(List<String> values) {
            addCriterion("now_address in", values, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressNotIn(List<String> values) {
            addCriterion("now_address not in", values, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressBetween(String value1, String value2) {
            addCriterion("now_address between", value1, value2, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressNotBetween(String value1, String value2) {
            addCriterion("now_address not between", value1, value2, "nowAddress");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailIsNull() {
            addCriterion("now_address_detail is null");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailIsNotNull() {
            addCriterion("now_address_detail is not null");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailEqualTo(String value) {
            addCriterion("now_address_detail =", value, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailNotEqualTo(String value) {
            addCriterion("now_address_detail <>", value, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailGreaterThan(String value) {
            addCriterion("now_address_detail >", value, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailGreaterThanOrEqualTo(String value) {
            addCriterion("now_address_detail >=", value, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailLessThan(String value) {
            addCriterion("now_address_detail <", value, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailLessThanOrEqualTo(String value) {
            addCriterion("now_address_detail <=", value, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailLike(String value) {
            addCriterion("now_address_detail like", value, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailNotLike(String value) {
            addCriterion("now_address_detail not like", value, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailIn(List<String> values) {
            addCriterion("now_address_detail in", values, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailNotIn(List<String> values) {
            addCriterion("now_address_detail not in", values, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailBetween(String value1, String value2) {
            addCriterion("now_address_detail between", value1, value2, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andNowAddressDetailNotBetween(String value1, String value2) {
            addCriterion("now_address_detail not between", value1, value2, "nowAddressDetail");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageIsNull() {
            addCriterion("employee_image is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageIsNotNull() {
            addCriterion("employee_image is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageEqualTo(String value) {
            addCriterion("employee_image =", value, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageNotEqualTo(String value) {
            addCriterion("employee_image <>", value, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageGreaterThan(String value) {
            addCriterion("employee_image >", value, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageGreaterThanOrEqualTo(String value) {
            addCriterion("employee_image >=", value, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageLessThan(String value) {
            addCriterion("employee_image <", value, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageLessThanOrEqualTo(String value) {
            addCriterion("employee_image <=", value, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageLike(String value) {
            addCriterion("employee_image like", value, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageNotLike(String value) {
            addCriterion("employee_image not like", value, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageIn(List<String> values) {
            addCriterion("employee_image in", values, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageNotIn(List<String> values) {
            addCriterion("employee_image not in", values, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageBetween(String value1, String value2) {
            addCriterion("employee_image between", value1, value2, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andEmployeeImageNotBetween(String value1, String value2) {
            addCriterion("employee_image not between", value1, value2, "employeeImage");
            return (Criteria) this;
        }

        public Criteria andTelphoneIsNull() {
            addCriterion("telphone is null");
            return (Criteria) this;
        }

        public Criteria andTelphoneIsNotNull() {
            addCriterion("telphone is not null");
            return (Criteria) this;
        }

        public Criteria andTelphoneEqualTo(String value) {
            addCriterion("telphone =", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotEqualTo(String value) {
            addCriterion("telphone <>", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneGreaterThan(String value) {
            addCriterion("telphone >", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneGreaterThanOrEqualTo(String value) {
            addCriterion("telphone >=", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLessThan(String value) {
            addCriterion("telphone <", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLessThanOrEqualTo(String value) {
            addCriterion("telphone <=", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLike(String value) {
            addCriterion("telphone like", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotLike(String value) {
            addCriterion("telphone not like", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneIn(List<String> values) {
            addCriterion("telphone in", values, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotIn(List<String> values) {
            addCriterion("telphone not in", values, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneBetween(String value1, String value2) {
            addCriterion("telphone between", value1, value2, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotBetween(String value1, String value2) {
            addCriterion("telphone not between", value1, value2, "telphone");
            return (Criteria) this;
        }

        public Criteria andContactNameIsNull() {
            addCriterion("contact_name is null");
            return (Criteria) this;
        }

        public Criteria andContactNameIsNotNull() {
            addCriterion("contact_name is not null");
            return (Criteria) this;
        }

        public Criteria andContactNameEqualTo(String value) {
            addCriterion("contact_name =", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameNotEqualTo(String value) {
            addCriterion("contact_name <>", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameGreaterThan(String value) {
            addCriterion("contact_name >", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameGreaterThanOrEqualTo(String value) {
            addCriterion("contact_name >=", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameLessThan(String value) {
            addCriterion("contact_name <", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameLessThanOrEqualTo(String value) {
            addCriterion("contact_name <=", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameLike(String value) {
            addCriterion("contact_name like", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameNotLike(String value) {
            addCriterion("contact_name not like", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameIn(List<String> values) {
            addCriterion("contact_name in", values, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameNotIn(List<String> values) {
            addCriterion("contact_name not in", values, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameBetween(String value1, String value2) {
            addCriterion("contact_name between", value1, value2, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameNotBetween(String value1, String value2) {
            addCriterion("contact_name not between", value1, value2, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneIsNull() {
            addCriterion("contact_telphone is null");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneIsNotNull() {
            addCriterion("contact_telphone is not null");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneEqualTo(String value) {
            addCriterion("contact_telphone =", value, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneNotEqualTo(String value) {
            addCriterion("contact_telphone <>", value, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneGreaterThan(String value) {
            addCriterion("contact_telphone >", value, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneGreaterThanOrEqualTo(String value) {
            addCriterion("contact_telphone >=", value, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneLessThan(String value) {
            addCriterion("contact_telphone <", value, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneLessThanOrEqualTo(String value) {
            addCriterion("contact_telphone <=", value, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneLike(String value) {
            addCriterion("contact_telphone like", value, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneNotLike(String value) {
            addCriterion("contact_telphone not like", value, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneIn(List<String> values) {
            addCriterion("contact_telphone in", values, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneNotIn(List<String> values) {
            addCriterion("contact_telphone not in", values, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneBetween(String value1, String value2) {
            addCriterion("contact_telphone between", value1, value2, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andContactTelphoneNotBetween(String value1, String value2) {
            addCriterion("contact_telphone not between", value1, value2, "contactTelphone");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeIsNull() {
            addCriterion("office_code is null");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeIsNotNull() {
            addCriterion("office_code is not null");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeEqualTo(String value) {
            addCriterion("office_code =", value, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeNotEqualTo(String value) {
            addCriterion("office_code <>", value, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeGreaterThan(String value) {
            addCriterion("office_code >", value, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("office_code >=", value, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeLessThan(String value) {
            addCriterion("office_code <", value, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeLessThanOrEqualTo(String value) {
            addCriterion("office_code <=", value, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeLike(String value) {
            addCriterion("office_code like", value, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeNotLike(String value) {
            addCriterion("office_code not like", value, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeIn(List<String> values) {
            addCriterion("office_code in", values, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeNotIn(List<String> values) {
            addCriterion("office_code not in", values, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeBetween(String value1, String value2) {
            addCriterion("office_code between", value1, value2, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeCodeNotBetween(String value1, String value2) {
            addCriterion("office_code not between", value1, value2, "officeCode");
            return (Criteria) this;
        }

        public Criteria andOfficeNameIsNull() {
            addCriterion("office_name is null");
            return (Criteria) this;
        }

        public Criteria andOfficeNameIsNotNull() {
            addCriterion("office_name is not null");
            return (Criteria) this;
        }

        public Criteria andOfficeNameEqualTo(String value) {
            addCriterion("office_name =", value, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameNotEqualTo(String value) {
            addCriterion("office_name <>", value, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameGreaterThan(String value) {
            addCriterion("office_name >", value, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameGreaterThanOrEqualTo(String value) {
            addCriterion("office_name >=", value, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameLessThan(String value) {
            addCriterion("office_name <", value, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameLessThanOrEqualTo(String value) {
            addCriterion("office_name <=", value, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameLike(String value) {
            addCriterion("office_name like", value, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameNotLike(String value) {
            addCriterion("office_name not like", value, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameIn(List<String> values) {
            addCriterion("office_name in", values, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameNotIn(List<String> values) {
            addCriterion("office_name not in", values, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameBetween(String value1, String value2) {
            addCriterion("office_name between", value1, value2, "officeName");
            return (Criteria) this;
        }

        public Criteria andOfficeNameNotBetween(String value1, String value2) {
            addCriterion("office_name not between", value1, value2, "officeName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameIsNull() {
            addCriterion("register_name is null");
            return (Criteria) this;
        }

        public Criteria andRegisterNameIsNotNull() {
            addCriterion("register_name is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterNameEqualTo(String value) {
            addCriterion("register_name =", value, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameNotEqualTo(String value) {
            addCriterion("register_name <>", value, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameGreaterThan(String value) {
            addCriterion("register_name >", value, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameGreaterThanOrEqualTo(String value) {
            addCriterion("register_name >=", value, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameLessThan(String value) {
            addCriterion("register_name <", value, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameLessThanOrEqualTo(String value) {
            addCriterion("register_name <=", value, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameLike(String value) {
            addCriterion("register_name like", value, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameNotLike(String value) {
            addCriterion("register_name not like", value, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameIn(List<String> values) {
            addCriterion("register_name in", values, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameNotIn(List<String> values) {
            addCriterion("register_name not in", values, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameBetween(String value1, String value2) {
            addCriterion("register_name between", value1, value2, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterNameNotBetween(String value1, String value2) {
            addCriterion("register_name not between", value1, value2, "registerName");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeIsNull() {
            addCriterion("register_time is null");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeIsNotNull() {
            addCriterion("register_time is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeEqualTo(Date value) {
            addCriterion("register_time =", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeNotEqualTo(Date value) {
            addCriterion("register_time <>", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeGreaterThan(Date value) {
            addCriterion("register_time >", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("register_time >=", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeLessThan(Date value) {
            addCriterion("register_time <", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeLessThanOrEqualTo(Date value) {
            addCriterion("register_time <=", value, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeIn(List<Date> values) {
            addCriterion("register_time in", values, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeNotIn(List<Date> values) {
            addCriterion("register_time not in", values, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeBetween(Date value1, Date value2) {
            addCriterion("register_time between", value1, value2, "registerTime");
            return (Criteria) this;
        }

        public Criteria andRegisterTimeNotBetween(Date value1, Date value2) {
            addCriterion("register_time not between", value1, value2, "registerTime");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeIsNull() {
            addCriterion("logout_office_code is null");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeIsNotNull() {
            addCriterion("logout_office_code is not null");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeEqualTo(String value) {
            addCriterion("logout_office_code =", value, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeNotEqualTo(String value) {
            addCriterion("logout_office_code <>", value, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeGreaterThan(String value) {
            addCriterion("logout_office_code >", value, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("logout_office_code >=", value, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeLessThan(String value) {
            addCriterion("logout_office_code <", value, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeLessThanOrEqualTo(String value) {
            addCriterion("logout_office_code <=", value, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeLike(String value) {
            addCriterion("logout_office_code like", value, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeNotLike(String value) {
            addCriterion("logout_office_code not like", value, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeIn(List<String> values) {
            addCriterion("logout_office_code in", values, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeNotIn(List<String> values) {
            addCriterion("logout_office_code not in", values, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeBetween(String value1, String value2) {
            addCriterion("logout_office_code between", value1, value2, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeCodeNotBetween(String value1, String value2) {
            addCriterion("logout_office_code not between", value1, value2, "logoutOfficeCode");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameIsNull() {
            addCriterion("logout_office_name is null");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameIsNotNull() {
            addCriterion("logout_office_name is not null");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameEqualTo(String value) {
            addCriterion("logout_office_name =", value, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameNotEqualTo(String value) {
            addCriterion("logout_office_name <>", value, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameGreaterThan(String value) {
            addCriterion("logout_office_name >", value, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameGreaterThanOrEqualTo(String value) {
            addCriterion("logout_office_name >=", value, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameLessThan(String value) {
            addCriterion("logout_office_name <", value, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameLessThanOrEqualTo(String value) {
            addCriterion("logout_office_name <=", value, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameLike(String value) {
            addCriterion("logout_office_name like", value, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameNotLike(String value) {
            addCriterion("logout_office_name not like", value, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameIn(List<String> values) {
            addCriterion("logout_office_name in", values, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameNotIn(List<String> values) {
            addCriterion("logout_office_name not in", values, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameBetween(String value1, String value2) {
            addCriterion("logout_office_name between", value1, value2, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutOfficeNameNotBetween(String value1, String value2) {
            addCriterion("logout_office_name not between", value1, value2, "logoutOfficeName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameIsNull() {
            addCriterion("logout_name is null");
            return (Criteria) this;
        }

        public Criteria andLogoutNameIsNotNull() {
            addCriterion("logout_name is not null");
            return (Criteria) this;
        }

        public Criteria andLogoutNameEqualTo(String value) {
            addCriterion("logout_name =", value, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameNotEqualTo(String value) {
            addCriterion("logout_name <>", value, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameGreaterThan(String value) {
            addCriterion("logout_name >", value, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameGreaterThanOrEqualTo(String value) {
            addCriterion("logout_name >=", value, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameLessThan(String value) {
            addCriterion("logout_name <", value, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameLessThanOrEqualTo(String value) {
            addCriterion("logout_name <=", value, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameLike(String value) {
            addCriterion("logout_name like", value, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameNotLike(String value) {
            addCriterion("logout_name not like", value, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameIn(List<String> values) {
            addCriterion("logout_name in", values, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameNotIn(List<String> values) {
            addCriterion("logout_name not in", values, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameBetween(String value1, String value2) {
            addCriterion("logout_name between", value1, value2, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutNameNotBetween(String value1, String value2) {
            addCriterion("logout_name not between", value1, value2, "logoutName");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeIsNull() {
            addCriterion("logout_time is null");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeIsNotNull() {
            addCriterion("logout_time is not null");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeEqualTo(Date value) {
            addCriterion("logout_time =", value, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeNotEqualTo(Date value) {
            addCriterion("logout_time <>", value, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeGreaterThan(Date value) {
            addCriterion("logout_time >", value, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("logout_time >=", value, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeLessThan(Date value) {
            addCriterion("logout_time <", value, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeLessThanOrEqualTo(Date value) {
            addCriterion("logout_time <=", value, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeIn(List<Date> values) {
            addCriterion("logout_time in", values, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeNotIn(List<Date> values) {
            addCriterion("logout_time not in", values, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeBetween(Date value1, Date value2) {
            addCriterion("logout_time between", value1, value2, "logoutTime");
            return (Criteria) this;
        }

        public Criteria andLogoutTimeNotBetween(Date value1, Date value2) {
            addCriterion("logout_time not between", value1, value2, "logoutTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}