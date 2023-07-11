package com.cg.utils;//package com.cg.utils;
//
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//@Component
//public class UniqueValidator implements ConstraintValidator<Unique, Object> {
//    @PersistenceContext
//    private EntityManager entityManager;
//    private String message;
//    private Class<?> domainClass;
//    private String fieldName;
//    @Override
//    public void initialize(Unique constraintAnnotation) {
//        this.message = constraintAnnotation.message();
//        this.domainClass = constraintAnnotation.domainClass();
//        this.fieldName = constraintAnnotation.fieldName();
//    }
//    @Override
//    @Transactional
//    public boolean isValid(Object value, ConstraintValidatorContext context) {
//        String jpql = "SELECT COUNT(u) = 0 FROM " + domainClass.getName() + " u WHERE u." + fieldName + " = :value";
//        try {
//            Query query = entityManager.createQuery(jpql);
//            query.setParameter("value", value);
//            entityManager.close();
//            return (Boolean) query.getSingleResult();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return true;
//        }
//    }
//}