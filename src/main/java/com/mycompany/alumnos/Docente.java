package com.mycompany.alumnos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "docente", catalog = "bdalumnos", schema = "")
public class Docente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddocente", nullable = false)
    private Integer iddocente;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false, length = 10)
    private Cargo cargo;

    // EAGER: el formulario de edición y los listados leen la colección fuera de la
    // transacción (JSP), donde una colección perezosa no estaría inicializable.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "docente_has_materia", catalog = "bdalumnos", schema = "",
            joinColumns = @JoinColumn(name = "docente_iddocente", referencedColumnName = "iddocente"),
            inverseJoinColumns = @JoinColumn(name = "materia_idmateria", referencedColumnName = "idmateria"))
    private Collection<Materia> materiaCollection;

    public Docente() { }

    public Integer getIddocente() { return iddocente; }
    public void setIddocente(Integer iddocente) { this.iddocente = iddocente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public Collection<Materia> getMateriaCollection() { return materiaCollection; }
    public void setMateriaCollection(Collection<Materia> materiaCollection) { this.materiaCollection = materiaCollection; }

    @Override
    public int hashCode() { return iddocente != null ? iddocente.hashCode() : 0; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Docente)) return false;
        Docente other = (Docente) o;
        return iddocente != null && iddocente.equals(other.iddocente);
    }

    @Override
    public String toString() { return "com.mycompany.alumnos.Docente[ iddocente=" + iddocente + " ]"; }
}