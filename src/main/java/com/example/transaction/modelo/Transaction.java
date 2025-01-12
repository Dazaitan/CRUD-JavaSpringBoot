package com.example.transaction.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
/*No hay necesidad de referenciar el nombre de la tabla porque en la BD y codigo tienen el mismo nombre
* Pero en dado caso haya discrepancia en el nomnbre de la tabla y clase utilizamos @Table(name = "nombre_tabla")
* */

@Entity //Definir que la clase es una entidad de JPA representando una tabla de la base de datos
@Data //Generar automaticamente los constructores y los setters/getters
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //especificar cómo se debe generar el valor de la clave primaria (ID) de una entidad
    private Long id;
    @Enumerated(EnumType.STRING) //Definir como se almacenera en la base de datos
    private Type type;
    private BigDecimal amount;
    private String category;
    @Temporal(TemporalType.DATE) // Especifica que el campo date se almacenará solo con la parte de la fecha, sin la hora.
    private Date date;
    private String description;

    public enum Type { Income, Expense }


}
