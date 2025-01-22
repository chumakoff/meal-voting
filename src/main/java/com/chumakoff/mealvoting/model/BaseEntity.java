package com.chumakoff.mealvoting.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (getId() == null) return false;
        return (getClass(this) == getClass(o)) && getId().equals(((BaseEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass(this).hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }

    private Class<?> getClass(Object object) {
        return (object instanceof HibernateProxy proxy) ?
                proxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
    }
}
