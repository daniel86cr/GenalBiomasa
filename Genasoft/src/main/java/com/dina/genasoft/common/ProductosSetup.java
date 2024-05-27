/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TCalibresProductos;
import com.dina.genasoft.db.entity.TDiametrosProducto;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TProductoCalibre;
import com.dina.genasoft.db.entity.TProductos;
import com.dina.genasoft.db.entity.TProductosVista;
import com.dina.genasoft.db.entity.TVariedad;
import com.dina.genasoft.db.mapper.TCalibresProductosMapper;
import com.dina.genasoft.db.mapper.TDiametrosProductoMapper;
import com.dina.genasoft.db.mapper.TProductoCalibreMapper;
import com.dina.genasoft.db.mapper.TProductoDiametroMapper;
import com.dina.genasoft.db.mapper.TProductoVariedadMapper;
import com.dina.genasoft.db.mapper.TProductosMapper;
import com.dina.genasoft.db.mapper.TVariedadMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de productos y la BD.
 */
@Component
@Slf4j
@Data
public class ProductosSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(ProductosSetup.class);
    /** Inyección por Spring del mapper TCalibresProductosMapper.*/
    @Autowired
    private TCalibresProductosMapper      tCalibresProductosMapper;
    /** Inyección por Spring del mapper TDiametrosProductoMapper.*/
    @Autowired
    private TDiametrosProductoMapper      tDiametrosProductoMapper;
    /** Inyección por Spring del mapper TProductosMapper.*/
    @Autowired
    private TProductosMapper              tProductosMapper;
    /** Inyección por Spring del mapper TProductoCalibreMapper.*/
    @Autowired
    private TProductoCalibreMapper        tProductoCalibreMapper;
    /** Inyección por Spring del mapper TProductoDiametroMapper.*/
    @Autowired
    private TProductoDiametroMapper       tProductoDiametroMapper;
    /** Inyección por Spring del mapper TProductoVariedadMapper.*/
    @Autowired
    private TProductoVariedadMapper       tProductoVariedadMapper;
    /** Inyección por Spring del mapper TVariedadMapper.*/
    @Autowired
    private TVariedadMapper               tVariedadMapper;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados.*/
    @Autowired
    private EmpleadosSetup                empleadosSetup;
    /** Serial ID de la aplicación Spring. */
    private static final long             serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el producto a partir del ID.
     * @param id El ID del producto
     * @return El producto encontrado.
     */
    public TProductos obtenerProductoPorId(Integer id) {
        return tProductosMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna los calibres de producto existentes en el sistema.
     * @return Los calibres de producto encontrados
     */
    public TProductoCalibre obtenerCalibrePorId(Integer idProducto, String calibre) {
        return tProductoCalibreMapper.selectByPrimaryKey(idProducto, calibre);
    }

    /**
     * Método que nos retorna el producto a partir del nombre.
     * @param nombre El nombre del producto
     * @return El producto encontrado.
     */
    public TProductos obtenerProductoPorNombre(String nombre) {
        return tProductosMapper.obtenerProductoPorNombre(nombre.trim().toUpperCase());
    }

    /**
     * Método que nos retorna los productos existentes en el sistema.
     * @return Los productos encontrados
     */
    public List<TProductos> obtenerTodosProductos() {
        return tProductosMapper.obtenerTodosProductos();
    }

    /**
     * Método que nos retorna las variedades que están asociadas al producto
     * @param idProducto El ID del producto a consultar
     * @return Las variedades encontradas.
     */
    public List<TVariedad> obtenerVariedadesProducto(Integer idProducto) {
        return tVariedadMapper.obtenerVariedadesIdProducto(idProducto);
    }

    public List<TProductoCalibre> obtenerNumerosCalibreProducto(Integer idProducto) {
        return tProductoCalibreMapper.obtenerCalibresProducto(idProducto);
    }

    public List<TDiametrosProducto> obtenerDiametrosProducto(Integer idProducto) {
        return tDiametrosProductoMapper.obtenerDiametrosProducto(idProducto);
    }

    public TDiametrosProducto obtenerDiametrosPorId(Integer id) {
        return tDiametrosProductoMapper.selectByPrimaryKey(id);
    }

    public List<TDiametrosProducto> obtenerTodosDimaetros() {
        return tDiametrosProductoMapper.obtenerTodosDiametros();
    }

    /**
     * Método que nos retorna las variedades de producto existentes en el sistema.
     * @return Las variedades de producto encontradas
     */
    public List<TVariedad> obtenerTodasVariedades() {
        return tVariedadMapper.obtenerTodasVariedades();
    }

    /**
     * Método que nos retorna los calibres de producto existentes en el sistema.
     * @return Los calibres de producto encontrados
     */
    public List<TCalibresProductos> obtenerTodosCalibres() {
        return tCalibresProductosMapper.obtenerTodosCalibres();
    }

    /**
     * Método que nos retorna los productos activos en el sistema.
     * @return Los productos activos encontrados
     */
    public List<TProductos> obtenerProductosActivos() {
        return tProductosMapper.obtenerProductosActivos();
    }

    /**
     * Método que nos retorna los productos existentes en el sistema en formato vista.
     * @return Los productos encontrados
     */
    public List<TProductosVista> obtenerTodosProductosVista() {
        return convertirProductosVista(obtenerTodosProductos());
    }

    /**
     * Método que nos retorna los productos activos en el sistema en formato vista.
     * @return Los productos activos encontrados
     */
    public List<TProductosVista> obtenerProductosActivosVista() {
        return convertirProductosVista(obtenerProductosActivos());
    }

    /**
     * Método que se encarga de crear el cliente en el sistema.
     * @param prod El cliente a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearProducto(TProductos prod) {
        String result = Constants.OPERACION_OK;
        prod.setDescripcion(prod.getDescripcion().trim().toUpperCase());

        String res = existeProducto(prod);

        if (res != null) {
            return res;
        } else {
            try {
                result = tProductosMapper.insert(prod) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_PRODUCTO;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_PRODUCTO;
                log.error(Constants.BD_KO_CREA_PRODUCTO + ", Error al crear el producto: " + prod.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que nos crea ekl producto en el sistema y nos retorna el ID del producto generado
     * @param prod El producto a crear
     * @return El ID del producto creado, si -1 es que no se ha podido crear.
     */
    public Integer crearProductoRetornaId(TProductos record) {
        Integer id = -1;

        record.setDescripcion(record.getDescripcion().trim().toUpperCase());
        // Comprobamos si existe el producto a crear
        TProductos aux = obtenerProductoPorNombre(record.getDescripcion());

        if (aux != null) {
            id = aux.getId();
        } else {
            try {
                // Rellenamos los datos necesarios para crear la familia.
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", 0);
                map.put("descripcion", record.getDescripcion());
                map.put("tipo", record.getTipo());
                map.put("usuCrea", record.getUsuCrea());
                map.put("fechaCrea", record.getFechaCrea());
                map.put("usuModifica", record.getUsuModifica());
                map.put("fechaModifica", record.getFechaModifica());
                map.put("estado", record.getEstado());

                tProductosMapper.insertRecord(map);

                id = (Integer) map.get("id");
            } catch (Exception e) {
                id = -1;
                log.error(Constants.BD_KO_CREA_PRODUCTO + ", Error al crear el producto: " + record.toString2() + ", ", e);
            }
        }

        return id;

    }

    /**
     * Método que se encarga de modificar el producto en el sistema.
     * @param record El producto a modificar.     
     * @return El código del resultado de la operación.
     */
    public String modificarProducto(TProductos record) {
        String result = Constants.OPERACION_OK;
        record.setDescripcion(record.getDescripcion().trim().toUpperCase());

        String res = existeProducto(record);

        if (res != null) {
            return res;
        } else {
            try {
                result = tProductosMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_PRODUCTO;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_PRODUCTO;
                log.error(Constants.BD_KO_MODIF_PRODUCTO + ", Error al modificar el producto: " + record.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de realizar la conversión de  {@linkplain TProductos} a  {@linkplain TProductosVista}
     * @param lProductos Lista de productos a convertir
     * @return Lista de productos en formato  {@linkplain TProductosVista}
     */
    private List<TProductosVista> convertirProductosVista(List<TProductos> lProductos) {
        List<TProductosVista> lResult = Utils.generarListaGenerica();

        TProductosVista aux = null;

        //List<TPais> lPaises = paisesSetup.obtenerTodosPaises();
        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));

        String campo = "";
        for (TProductos prod : lProductos) {
            aux = new TProductosVista();
            aux.copiaDesdeProducto(prod);

            campo = mEmpleados.get(prod.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "Empleado no encontrado; ID: " + prod.getUsuCrea());

            if (prod.getUsuModifica() != null) {
                campo = mEmpleados.get(prod.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "Empleado no encontrado; ID: " + prod.getUsuModifica());
            }

            lResult.add(aux);
        }

        return lResult;
    }

    /**
     * Método que nos comprueba si existe el producto en el sistema
     * @param prod El producto a comprobar por el campo nombre
     * @return El código del resultado de la operación.
     */
    private String existeProducto(TProductos prod) {
        String result = null;
        TProductos aux = obtenerProductoPorNombre(prod.getDescripcion());
        if (aux == null || (prod.getId() != null && prod.getId().equals(aux.getId()))) {

        } else {
            result = Constants.PRODUCTO_EXISTE_NOMBRE;
        }

        return result;
    }

}