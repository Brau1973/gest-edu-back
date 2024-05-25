//package com.tecnoinf.gestedu.models;
//
//import lombok.Data;
//import org.springframework.data.annotation.Version;
//import org.springframework.data.neo4j.core.schema.Id;
//import org.springframework.data.neo4j.core.schema.Node;
//import org.springframework.data.neo4j.core.schema.Relationship;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Data
//@Node
//public class AsignaturaNeo {
//
//    @Id
//    private Long id;
//    private String nombre;
//
////    @Version
////    private Long version;
//
//    @Relationship(type = "PREVIA", direction = Relationship.Direction.OUTGOING)
//    private List<AsignaturaNeo> asignaturasPrevias = new ArrayList<>();
//
//}