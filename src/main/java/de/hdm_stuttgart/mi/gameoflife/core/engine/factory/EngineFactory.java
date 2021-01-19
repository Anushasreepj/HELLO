package de.hdm_stuttgart.mi.gameoflife.core.engine.factory;
import de.hdm_stuttgart.mi.gameoflife.core.Grid;
import de.hdm_stuttgart.mi.gameoflife.core.IEngine;
import de.hdm_stuttgart.mi.gameoflife.core.IGrid;
import de.hdm_stuttgart.mi.gameoflife.core.engine.Engine;
import de.hdm_stuttgart.mi.gameoflife.core.engine.StreamEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class EngineFactory {

    private static final Logger logger = LogManager.getLogger(EngineFactory.class);


    private EngineFactory(){} //prevent manual instantiation of this class

    /**
     * Will return the engine with the specified grid loaded.
     *
     * Engine Names are not case-sensitive.
     * StreamEngine - de.hdm_stuttgart.me.gameoflife.core.engine.StreamEngine (An Engine using JavaStreams, making use of parallel streams)
     * Engine - de.hdm_stuttgart.me.gameoflife.core.engine.Engine (Default Engine, making use of for loops and runnables)
     *
     * @param name The name of the engine type
     * @param grid The grid to load on
     * @return Engine with loaded grid
     */
    public static IEngine loadByName(String name, IGrid grid) throws EngineNotFoundException {

        logger.trace("Try to get engine by name: " + name);

        name = name.toLowerCase(Locale.ENGLISH);

        switch (name){
            case "streamengine":
                return new StreamEngine(grid);
            case "engine":
                return new Engine(grid);
        }


        throw new EngineNotFoundException("Engine with the name " + name + " hasn't been found");

    }

    /**
     * Will return the engine with an empty IGrid of Type de.hdm_stuttgart.me.gameoflife.core.Grid
     *
     * Engine Names are not case-sensitive.
     * StreamEngine - de.hdm_stuttgart.me.gameoflife.core.engine.StreamEngine (An Engine using JavaStreams, making use of parallel streams)
     * Engine - de.hdm_stuttgart.me.gameoflife.core.engine.Engine (Default Engine, making use of for loops and runnables)
     *
     * @param name The name of the engine type
     * @return Engine with empty grid
     */
    public static IEngine loadByName(String name) throws EngineNotFoundException {
        return loadByName(name, new Grid());
    }


    /**
     * Will try to return an instance of the engine class specified by the class name with the default grid loaded.
     * The Engine Class needs to implement de.hdm_stuttgart.me.gameoflife.core.IEngine
     * @param name Class name of Engine
     * @return Engine Instance
     * @throws EngineNotFoundException
     */
    public static IEngine loadByReflection(String name) throws EngineNotFoundException {
        return loadByReflection(name, null);
    }

    /**
     * Will try to return an instance of the engine specified by the class name with the given grid loaded.
     * The Engine Class needs to implement de.hdm_stuttgart.me.gameoflife.core.IEngine
     * @param name Class name of Engine
     * @return Engine Instance
     * @throws EngineNotFoundException
     */
    public static IEngine loadByReflection(String name, IGrid grid) throws EngineNotFoundException {

        logger.trace("Try to get engine by class name: " + name);

        try {
            Class<?> engineClass = Class.forName(name);

            if(IEngine.class.isAssignableFrom(engineClass)){

                Constructor<?>[] constructors = engineClass.getConstructors();

                IEngine engine =  (IEngine) engineClass.getDeclaredConstructor().newInstance();

                if(grid != null) engine.loadGrid(grid);
                return engine;

            } else {
                throw new EngineNotFoundException("Engine with the class name " + name + " has been found but doesn't seem to implement IEngine.");
            }

        } catch (ClassNotFoundException e){
            throw new EngineNotFoundException("Engine with the class name " + name + " hasn't been found");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new EngineNotFoundException("Engine with the class name " + name + " is an engine but no new instance could be created. Exception: " + e.getMessage());
        }
    }


}
