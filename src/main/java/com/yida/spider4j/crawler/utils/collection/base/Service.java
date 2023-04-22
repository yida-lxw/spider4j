package com.yida.spider4j.crawler.utils.collection.base;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;

/**
 * An object with an operational state, plus asynchronous {@link #start()} and
 * {@link #stop()} lifecycle methods to transfer into and out of this state.
 * Example services include webservers, RPC servers and timers. The normal
 * lifecycle of a service is:
 * <ul>
 *   <li>{@link State#NEW} -&gt;</li>
 *   <li>{@link State#STARTING} -&gt;</li>
 *   <li>{@link State#RUNNING} -&gt;</li>
 *   <li>{@link State#STOPPING} -&gt;</li>
 *   <li>{@link State#TERMINATED}</li>
 * </ul>
 *
 * If the service fails while starting, running or stopping, its state will be
 * {@link State#FAILED}, and its behavior is undefined. Such a service cannot be
 * started nor stopped.
 *
 * <p>Implementors of this interface are strongly encouraged to extend {@link
 * com.google.common.util.concurrent.AbstractService} or {@link
 * com.google.common.util.concurrent.AbstractExecutionThreadService}, which make
 * the threading and state management easier.
 *
 * @author Jesse Wilson
 * @since 1
 */
@Beta // TODO(kevinb): make abstract class? move to common.util.concurrent?
public interface Service {
  /**
   * If the service state is {@link State#NEW}, this initiates service startup
   * and returns immediately. If the service has already been started, this
   * method returns immediately without taking action. A stopped service may not
   * be restarted.
   *
   * @return a future for the startup result, regardless of whether this call
   *     initiated startup. Calling {@link Future#get} will block until the
   *     service has finished starting, and returns one of {@link
   *     State#RUNNING}, {@link State#STOPPING} or {@link State#TERMINATED}. If
   *     the service fails to start, {@link Future#get} will throw an {@link
   *     ExecutionException}, and the service's state will be {@link
   *     State#FAILED}. If it has already finished starting, {@link Future#get}
   *     returns immediately. Cancelling the returned future is unsupported and
   *     always returns {@code false}.
   */
  Future<State> start();

  /**
   * Initiates service startup (if necessary), returning once the service has
   * finished starting. Unlike calling {@code start().get()}, this method throws
   * no checked exceptions.
   *
   * @throws InterruptedRuntimeException if the thread was interrupted while
   *      waiting for the service to finish starting up.
   * @throws RuntimeException if startup failed
   * @return the state of the service when startup finished.
   */
  State startAndWait();

  /**
   * Returns {@code true} if this service is {@link State#RUNNING running}.
   */
  boolean isRunning();

  /**
   * Returns the lifecycle state of the service.
   */
  State state();

  /**
   * If the service is {@link State#STARTING} or {@link State#RUNNING}, this
   * initiates service shutdown and returns immediately. If this is {@link
   * State#NEW}, it is {@link State#TERMINATED terminated} without having been
   * started nor stopped.  If the service has already been stopped, this
   * method returns immediately without taking action.
   *
   * @return a future for the shutdown result, regardless of whether this call
   *     initiated shutdown. Calling {@link Future#get} will block until the
   *     service has finished shutting down, and either returns {@link
   *     State#TERMINATED} or throws an {@link ExecutionException}. If it has
   *     already finished stopping, {@link Future#get} returns immediately.
   *     Cancelling this future is unsupported and always returns {@code
   *     false}.
   */
  Future<State> stop();

  /**
   * Initiates service shutdown (if necessary), returning once the service has
   * finished stopping. If this is {@link State#STARTING}, startup will be
   * cancelled. If this is {@link State#NEW}, it is {@link State#TERMINATED
   * terminated} without having been started nor stopped. Unlike calling {@code
   * stop().get()}, this method throws no checked exceptions.
   *
   * @throws InterruptedRuntimeException if the thread was interrupted while
   *      waiting for the service to finish shutting down.
   * @throws RuntimeException if shutdown failed
   * @return the state of the service when shutdown finished.
   */
  State stopAndWait();

  /**
   * The lifecycle states of a service.
   *
   * @since 1
   */
  @Beta // should come out of Beta when Service does
  enum State {
    /**
     * A service in this state is inactive. It does minimal work and consumes
     * minimal resources.
     */
    NEW,

    /**
     * A service in this state is transitioning to {@link #RUNNING}.
     */
    STARTING,

    /**
     * A service in this state is operational.
     */
    RUNNING,

    /**
     * A service in this state is transitioning to {@link #TERMINATED}.
     */
    STOPPING,

    /**
     * A service in this state has completed execution normally. It does minimal
     * work and consumes minimal resources.
     */
    TERMINATED,

    /**
     * A service in this state has encountered a problem and may not be
     * operational. It cannot be started nor stopped.
     */
    FAILED
  }
}
