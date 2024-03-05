package vn.edu.tdtu.musicapplication.mappers;

public interface Mapper<O, D> {
    public O mapToObject(D dto);
    public D mapToDto(O object);
}
