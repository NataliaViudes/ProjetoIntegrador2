
package pi2.example.back_end.db;

import java.util.List;

public interface IDAL<T> {
    boolean gravar(T entidade);
    boolean alterar(T entidade);
    boolean apagar(T entidade);
    T get(int id);
    List<T> get(String filtro);
}
