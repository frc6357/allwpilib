// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.math.system;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Num;
import edu.wpi.first.math.numbers.N1;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.Function;

public final class NumericalIntegration {
  private NumericalIntegration() {
    // utility Class
  }

  /**
   * Performs Runge Kutta integration (4th order).
   *
   * @param f The function to integrate, which takes one argument x.
   * @param x The initial value of x.
   * @param dtSeconds The time over which to integrate.
   * @return the integration of dx/dt = f(x) for dt.
   */
  @SuppressWarnings("ParameterName")
  public static double rk4(DoubleFunction<Double> f, double x, double dtSeconds) {
    final var halfDt = 0.5 * dtSeconds;
    final var k1 = f.apply(x);
    final var k2 = f.apply(x + k1 * halfDt);
    final var k3 = f.apply(x + k2 * halfDt);
    final var k4 = f.apply(x + k3 * dtSeconds);
    return x + dtSeconds / 6.0 * (k1 + 2.0 * k2 + 2.0 * k3 + k4);
  }

  /**
   * Performs Runge Kutta integration (4th order).
   *
   * @param f The function to integrate. It must take two arguments x and u.
   * @param x The initial value of x.
   * @param u The value u held constant over the integration period.
   * @param dtSeconds The time over which to integrate.
   * @return The result of Runge Kutta integration (4th order).
   */
  @SuppressWarnings("ParameterName")
  public static double rk4(
      BiFunction<Double, Double, Double> f, double x, Double u, double dtSeconds) {
    final var halfDt = 0.5 * dtSeconds;
    final var k1 = f.apply(x, u);
    final var k2 = f.apply(x + k1 * halfDt, u);
    final var k3 = f.apply(x + k2 * halfDt, u);
    final var k4 = f.apply(x + k3 * dtSeconds, u);
    return x + dtSeconds / 6.0 * (k1 + 2.0 * k2 + 2.0 * k3 + k4);
  }

  /**
   * Performs 4th order Runge-Kutta integration of dx/dt = f(x, u) for dt.
   *
   * @param <States> A Num representing the states of the system to integrate.
   * @param <Inputs> A Num representing the inputs of the system to integrate.
   * @param f The function to integrate. It must take two arguments x and u.
   * @param x The initial value of x.
   * @param u The value u held constant over the integration period.
   * @param dtSeconds The time over which to integrate.
   * @return the integration of dx/dt = f(x, u) for dt.
   */
  @SuppressWarnings({"ParameterName", "MethodTypeParameterName"})
  public static <States extends Num, Inputs extends Num> Matrix<States, N1> rk4(
      BiFunction<Matrix<States, N1>, Matrix<Inputs, N1>, Matrix<States, N1>> f,
      Matrix<States, N1> x,
      Matrix<Inputs, N1> u,
      double dtSeconds) {
    final var halfDt = 0.5 * dtSeconds;
    Matrix<States, N1> k1 = f.apply(x, u);
    Matrix<States, N1> k2 = f.apply(x.plus(k1.times(halfDt)), u);
    Matrix<States, N1> k3 = f.apply(x.plus(k2.times(halfDt)), u);
    Matrix<States, N1> k4 = f.apply(x.plus(k3.times(dtSeconds)), u);
    return x.plus((k1.plus(k2.times(2.0)).plus(k3.times(2.0)).plus(k4)).times(dtSeconds).div(6.0));
  }

  /**
   * Performs 4th order Runge-Kutta integration of dx/dt = f(x) for dt.
   *
   * @param <States> A Num prepresenting the states of the system.
   * @param f The function to integrate. It must take one argument x.
   * @param x The initial value of x.
   * @param dtSeconds The time over which to integrate.
   * @return 4th order Runge-Kutta integration of dx/dt = f(x) for dt.
   */
  @SuppressWarnings({"ParameterName", "MethodTypeParameterName"})
  public static <States extends Num> Matrix<States, N1> rk4(
      Function<Matrix<States, N1>, Matrix<States, N1>> f, Matrix<States, N1> x, double dtSeconds) {
    final var halfDt = 0.5 * dtSeconds;
    Matrix<States, N1> k1 = f.apply(x);
    Matrix<States, N1> k2 = f.apply(x.plus(k1.times(halfDt)));
    Matrix<States, N1> k3 = f.apply(x.plus(k2.times(halfDt)));
    Matrix<States, N1> k4 = f.apply(x.plus(k3.times(dtSeconds)));
    return x.plus((k1.plus(k2.times(2.0)).plus(k3.times(2.0)).plus(k4)).times(dtSeconds).div(6.0));
  }

  /**
   * Performs adaptive RKF45 integration of dx/dt = f(x, u) for dt, as described in
   * https://en.wikipedia.org/wiki/Runge%E2%80%93Kutta%E2%80%93Fehlberg_method. By default, the max
   * error is 1e-6.
   *
   * @param <States> A Num representing the states of the system to integrate.
   * @param <Inputs> A Num representing the inputs of the system to integrate.
   * @param f The function to integrate. It must take two arguments x and u.
   * @param x The initial value of x.
   * @param u The value u held constant over the integration period.
   * @param dtSeconds The time over which to integrate.
   * @return the integration of dx/dt = f(x, u) for dt.
   */
  @SuppressWarnings("MethodTypeParameterName")
  public static <States extends Num, Inputs extends Num> Matrix<States, N1> rkf45(
      BiFunction<Matrix<States, N1>, Matrix<Inputs, N1>, Matrix<States, N1>> f,
      Matrix<States, N1> x,
      Matrix<Inputs, N1> u,
      double dtSeconds) {
    return rkf45(f, x, u, dtSeconds, 1e-6);
  }

  /**
   * Performs adaptive RKF45 integration of dx/dt = f(x, u) for dt, as described in
   * https://en.wikipedia.org/wiki/Runge%E2%80%93Kutta%E2%80%93Fehlberg_method
   *
   * @param <States> A Num representing the states of the system to integrate.
   * @param <Inputs> A Num representing the inputs of the system to integrate.
   * @param f The function to integrate. It must take two arguments x and u.
   * @param x The initial value of x.
   * @param u The value u held constant over the integration period.
   * @param dtSeconds The time over which to integrate.
   * @param maxError The maximum acceptable truncation error. Usually a small number like 1e-6.
   * @return the integration of dx/dt = f(x, u) for dt.
   */
  @SuppressWarnings("MethodTypeParameterName")
  public static <States extends Num, Inputs extends Num> Matrix<States, N1> rkf45(
      BiFunction<Matrix<States, N1>, Matrix<Inputs, N1>, Matrix<States, N1>> f,
      Matrix<States, N1> x,
      Matrix<Inputs, N1> u,
      double dtSeconds,
      double maxError) {
    // See
    // https://en.wikipedia.org/wiki/Runge%E2%80%93Kutta%E2%80%93Fehlberg_method
    // for the Butcher tableau the following arrays came from.

    // This is used for time-varying integration
    // // final double[5]
    // final double[] A = {
    //     1.0 / 4.0, 3.0 / 8.0, 12.0 / 13.0, 1.0, 1.0 / 2.0};

    // final double[5][5]
    final double[][] B = {
      {1.0 / 4.0},
      {3.0 / 32.0, 9.0 / 32.0},
      {1932.0 / 2197.0, -7200.0 / 2197.0, 7296.0 / 2197.0},
      {439.0 / 216.0, -8.0, 3680.0 / 513.0, -845.0 / 4104.0},
      {-8.0 / 27.0, 2.0, -3544.0 / 2565.0, 1859.0 / 4104.0, -11.0 / 40.0}
    };

    // final double[6]
    final double[] C1 = {
      16.0 / 135.0, 0.0, 6656.0 / 12825.0, 28561.0 / 56430.0, -9.0 / 50.0, 2.0 / 55.0
    };

    // final double[6]
    final double[] C2 = {25.0 / 216.0, 0.0, 1408.0 / 2565.0, 2197.0 / 4104.0, -1.0 / 5.0, 0.0};

    Matrix<States, N1> newX;
    double truncationError;

    double dtElapsed = 0.0;
    double h = dtSeconds;

    // Loop until we've gotten to our desired dt
    while (dtElapsed < dtSeconds) {
      do {
        // Only allow us to advance up to the dt remaining
        h = Math.min(h, dtSeconds - dtElapsed);

        // Notice how the derivative in the Wikipedia notation is dy/dx.
        // That means their y is our x and their x is our t
        var k1 = f.apply(x, u).times(h);
        var k2 = f.apply(x.plus(k1.times(B[0][0])), u).times(h);
        var k3 = f.apply(x.plus(k1.times(B[1][0])).plus(k2.times(B[1][1])), u).times(h);
        var k4 =
            f.apply(x.plus(k1.times(B[2][0])).plus(k2.times(B[2][1])).plus(k3.times(B[2][2])), u)
                .times(h);
        var k5 =
            f.apply(
                    x.plus(k1.times(B[3][0]))
                        .plus(k2.times(B[3][1]))
                        .plus(k3.times(B[3][2]))
                        .plus(k4.times(B[3][3])),
                    u)
                .times(h);
        var k6 =
            f.apply(
                    x.plus(k1.times(B[4][0]))
                        .plus(k2.times(B[4][1]))
                        .plus(k3.times(B[4][2]))
                        .plus(k4.times(B[4][3]))
                        .plus(k5.times(B[4][4])),
                    u)
                .times(h);

        newX =
            x.plus(k1.times(C1[0]))
                .plus(k2.times(C1[1]))
                .plus(k3.times(C1[2]))
                .plus(k4.times(C1[3]))
                .plus(k5.times(C1[4]))
                .plus(k6.times(C1[5]));
        truncationError =
            (k1.times(C1[0] - C2[0])
                    .plus(k2.times(C1[1] - C2[1]))
                    .plus(k3.times(C1[2] - C2[2]))
                    .plus(k4.times(C1[3] - C2[3]))
                    .plus(k5.times(C1[4] - C2[4]))
                    .plus(k6.times(C1[5] - C2[5])))
                .normF();

        h = 0.9 * h * Math.pow(maxError / truncationError, 1.0 / 5.0);
      } while (truncationError > maxError);

      dtElapsed += h;
      x = newX;
    }

    return x;
  }

  /**
   * Performs adaptive Dormand-Prince integration of dx/dt = f(x, u) for dt. By default, the max
   * error is 1e-6.
   *
   * @param <States> A Num representing the states of the system to integrate.
   * @param <Inputs> A Num representing the inputs of the system to integrate.
   * @param f The function to integrate. It must take two arguments x and u.
   * @param x The initial value of x.
   * @param u The value u held constant over the integration period.
   * @param dtSeconds The time over which to integrate.
   * @return the integration of dx/dt = f(x, u) for dt.
   */
  @SuppressWarnings("MethodTypeParameterName")
  public static <States extends Num, Inputs extends Num> Matrix<States, N1> rkdp(
      BiFunction<Matrix<States, N1>, Matrix<Inputs, N1>, Matrix<States, N1>> f,
      Matrix<States, N1> x,
      Matrix<Inputs, N1> u,
      double dtSeconds) {
    return rkdp(f, x, u, dtSeconds, 1e-6);
  }

  /**
   * Performs adaptive Dormand-Prince integration of dx/dt = f(x, u) for dt.
   *
   * @param <States> A Num representing the states of the system to integrate.
   * @param <Inputs> A Num representing the inputs of the system to integrate.
   * @param f The function to integrate. It must take two arguments x and u.
   * @param x The initial value of x.
   * @param u The value u held constant over the integration period.
   * @param dtSeconds The time over which to integrate.
   * @param maxError The maximum acceptable truncation error. Usually a small number like 1e-6.
   * @return the integration of dx/dt = f(x, u) for dt.
   */
  @SuppressWarnings("MethodTypeParameterName")
  public static <States extends Num, Inputs extends Num> Matrix<States, N1> rkdp(
      BiFunction<Matrix<States, N1>, Matrix<Inputs, N1>, Matrix<States, N1>> f,
      Matrix<States, N1> x,
      Matrix<Inputs, N1> u,
      double dtSeconds,
      double maxError) {
    // See https://en.wikipedia.org/wiki/Dormand%E2%80%93Prince_method for the
    // Butcher tableau the following arrays came from.

    // This is used for time-varying integration
    // // final double[6]
    // final double[] A = {
    //     1.0 / 5.0, 3.0 / 10.0, 4.0 / 5.0, 8.0 / 9.0, 1.0, 1.0};

    // final double[6][6]
    final double[][] B = {
      {1.0 / 5.0},
      {3.0 / 40.0, 9.0 / 40.0},
      {44.0 / 45.0, -56.0 / 15.0, 32.0 / 9.0},
      {19372.0 / 6561.0, -25360.0 / 2187.0, 64448.0 / 6561.0, -212.0 / 729.0},
      {9017.0 / 3168.0, -355.0 / 33.0, 46732.0 / 5247.0, 49.0 / 176.0, -5103.0 / 18656.0},
      {35.0 / 384.0, 0.0, 500.0 / 1113.0, 125.0 / 192.0, -2187.0 / 6784.0, 11.0 / 84.0}
    };

    // final double[7]
    final double[] C1 = {
      35.0 / 384.0, 0.0, 500.0 / 1113.0, 125.0 / 192.0, -2187.0 / 6784.0, 11.0 / 84.0, 0.0
    };

    // final double[7]
    final double[] C2 = {
      5179.0 / 57600.0,
      0.0,
      7571.0 / 16695.0,
      393.0 / 640.0,
      -92097.0 / 339200.0,
      187.0 / 2100.0,
      1.0 / 40.0
    };

    Matrix<States, N1> newX;
    double truncationError;

    double dtElapsed = 0.0;
    double h = dtSeconds;

    // Loop until we've gotten to our desired dt
    while (dtElapsed < dtSeconds) {
      do {
        // Only allow us to advance up to the dt remaining
        h = Math.min(h, dtSeconds - dtElapsed);

        var k1 = f.apply(x, u).times(h);
        var k2 = f.apply(x.plus(k1.times(B[0][0])), u).times(h);
        var k3 = f.apply(x.plus(k1.times(B[1][0])).plus(k2.times(B[1][1])), u).times(h);
        var k4 =
            f.apply(x.plus(k1.times(B[2][0])).plus(k2.times(B[2][1])).plus(k3.times(B[2][2])), u)
                .times(h);
        var k5 =
            f.apply(
                    x.plus(k1.times(B[3][0]))
                        .plus(k2.times(B[3][1]))
                        .plus(k3.times(B[3][2]))
                        .plus(k4.times(B[3][3])),
                    u)
                .times(h);
        var k6 =
            f.apply(
                    x.plus(k1.times(B[4][0]))
                        .plus(k2.times(B[4][1]))
                        .plus(k3.times(B[4][2]))
                        .plus(k4.times(B[4][3]))
                        .plus(k5.times(B[4][4])),
                    u)
                .times(h);

        // Since the final row of B and the array C1 have the same coefficients
        // and k7 has no effect on newX, we can reuse the calculation.
        newX =
            x.plus(k1.times(B[5][0]))
                .plus(k2.times(B[5][1]))
                .plus(k3.times(B[5][2]))
                .plus(k4.times(B[5][3]))
                .plus(k5.times(B[5][4]))
                .plus(k6.times(B[5][5]));
        var k7 = f.apply(newX, u).times(h);

        truncationError =
            (k1.times(C1[0] - C2[0])
                    .plus(k2.times(C1[1] - C2[1]))
                    .plus(k3.times(C1[2] - C2[2]))
                    .plus(k4.times(C1[3] - C2[3]))
                    .plus(k5.times(C1[4] - C2[4]))
                    .plus(k6.times(C1[5] - C2[5]))
                    .plus(k7.times(C1[6] - C2[6])))
                .normF();

        h = 0.9 * h * Math.pow(maxError / truncationError, 1.0 / 5.0);
      } while (truncationError > maxError);

      dtElapsed += h;
      x = newX;
    }

    return x;
  }
}
