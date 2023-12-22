package aandrosov.cbs.model;

import java.io.Serializable;

public record Book(long id, String title, String description, String coverUrl) implements Serializable {   
}