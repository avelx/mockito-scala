package org.mockito
package stubbing

import org.mockito.internal.ValueClassExtractor
import org.mockito.internal.stubbing.OngoingStubbingImpl
import org.mockito.internal.util.MockUtil.getMockSettings
import org.mockito.invocation.InvocationOnMock
import org.mockito.quality.Strictness.LENIENT

import scala.reflect.ClassTag

object ScalaFirstStubbing {
  implicit def toScalaFirstStubbing[T: ValueClassExtractor](v: OngoingStubbing[T]): ScalaFirstStubbing[T] = ScalaFirstStubbing(v)
  implicit def toMock[T](s: ScalaFirstStubbing[_]): T                                                     = s.getMock[T]
}

case class ScalaFirstStubbing[T: ValueClassExtractor](delegate: OngoingStubbing[T]) extends ScalaBaseStubbing[T] {

  //noinspection AccessorLikeMethodIsUnit
  def isLenient(): Unit = {
    delegate.asInstanceOf[OngoingStubbingImpl[T]].setStrictness(LENIENT)
    delegate.thenAnswer(new Answer[T] {
      override def answer(i: InvocationOnMock): T = getMockSettings(delegate.getMock[Any]).getDefaultAnswer.answer(i).asInstanceOf[T]
    })
  }

  /**
   * Sets consecutive return one or more values to be returned when the method is called. E.g:
   * <pre class="code"><code class="scala">
   * when(mock.someMethod) thenReturn 1
   * </code></pre>
   * <pre class="code"><code class="scala">
   * when(mock.someMethod) thenReturn (1, 2, 3)
   * </code></pre>
   *
   * Last return value in the sequence (in example: 3) determines the behavior of further consecutive calls.
   * <p>
   * See examples in javadoc for {@link org.mockito.MockitoSugar#when}
   *
   * @param value first return value
   * @param values next return values
   * @return object that allows stubbing consecutive calls
   */
  def thenReturn(value: T, values: T*): ScalaOngoingStubbing[T] = _thenReturn(value, values)

  /**
   * Sets one or more Throwable objects to be thrown when the method is called. E.g:
   * <pre class="code"><code class="java">
   * when(mock.someMethod) thenThrow new RuntimeException
   * </code></pre>
   * <pre class="code"><code class="java">
   * when(mock.someMethod) thenThrow (new RuntimeException, new OtherException)
   * </code></pre>
   *
   * <p>
   * You can specify throwables to be thrown for consecutive calls.
   * In that case the last throwable determines the behavior of further consecutive calls.
   * <p>
   * If throwable is null then exception will be thrown.
   * <p>
   * See examples in javadoc for {@link org.mockito.MockitoSugar#when}
   *
   * @param throwables to be thrown on method invocation
   * @return object that allows stubbing consecutive calls
   */
  def thenThrow(throwables: Throwable*): ScalaOngoingStubbing[T] = _thenThrow(throwables)

  /**
   * Sets a Throwable type to be thrown when the method is called. E.g:
   * <pre class="code"><code class="java">
   * when(mock.someMethod()).thenThrow(RuntimeException.class);
   * </code></pre>
   *
   * <p>
   * If the throwable class is a checked exception then it has to
   * match one of the checked exceptions of the stubbed method signature.
   * <p>
   * If throwable is null then exception will be thrown.
   * <p>
   * See examples in javadoc for {@link Mockito#when}
   *
   * <p>Note depending on the JVM, stack trace information may not be available in
   * the generated throwable instance.  If you require stack trace information,
   * use {@link OngoingStubbing#thenThrow(Throwable...)} instead.
   *
   * @param throwableType to be thrown on method invocation
   * @return object that allows stubbing consecutive calls
   */
  def thenThrow[E <: Throwable: ClassTag]: ScalaOngoingStubbing[T] = _thenThrow

  /**
   * Sets the real implementation to be called when the method is called on a mock object.
   * <p>
   * As usual you are going to read <b>the partial mock warning</b>:
   * Object oriented programming is more less tackling complexity by dividing the complexity into separate, specific, SRPy objects.
   * How does partial mock fit into this paradigm? Well, it just doesn't...
   * Partial mock usually means that the complexity has been moved to a different method on the same object.
   * In most cases, this is not the way you want to design your application.
   * <p>
   * However, there are rare cases when partial mocks come handy:
   * dealing with code you cannot change easily (3rd party interfaces, interim refactoring of legacy code etc.)
   * However, I wouldn't use partial mocks for new, test-driven & well-designed code.
   * <pre class="code"><code class="java">
   *   // someMethod() must be safe (e.g. doesn't throw, doesn't have dependencies to the object state, etc.)
   *   // if it isn't safe then you will have trouble stubbing it using this api. Use Mockito.doCallRealMethod() instead.
   *   when(mock.someMethod()).thenCallRealMethod();
   *
   *   // calls real method:
   *   mock.someMethod();
   *
   * </code></pre>
   * See also javadoc {@link Mockito#spy(Object)} to find out more about partial mocks.
   * <b>Mockito.spy() is a recommended way of creating partial mocks.</b>
   * The reason is it guarantees real methods are called against correctly constructed object because you're responsible for constructing the object passed to spy() method.
   * <p>
   * See examples in javadoc for {@link Mockito#when}
   *
   * @return object that allows stubbing consecutive calls
   */
  def thenCallRealMethod(): ScalaOngoingStubbing[T] = _thenCallRealMethod()

  def thenAnswer(f: => T): ScalaOngoingStubbing[T] = _thenAnswer(f)
  def thenAnswer[P0](f: P0 => T)(implicit classTag: ClassTag[P0] = defaultClassTag[P0]): ScalaOngoingStubbing[T] =
    _thenAnswer(f)
  def thenAnswer[P0, P1](f: (P0, P1) => T): ScalaOngoingStubbing[T]                                                         = _thenAnswer(f)
  def thenAnswer[P0, P1, P2](f: (P0, P1, P2) => T): ScalaOngoingStubbing[T]                                                 = _thenAnswer(f)
  def thenAnswer[P0, P1, P2, P3](f: (P0, P1, P2, P3) => T): ScalaOngoingStubbing[T]                                         = _thenAnswer(f)
  def thenAnswer[P0, P1, P2, P3, P4](f: (P0, P1, P2, P3, P4) => T): ScalaOngoingStubbing[T]                                 = _thenAnswer(f)
  def thenAnswer[P0, P1, P2, P3, P4, P5](f: (P0, P1, P2, P3, P4, P5) => T): ScalaOngoingStubbing[T]                         = _thenAnswer(f)
  def thenAnswer[P0, P1, P2, P3, P4, P5, P6](f: (P0, P1, P2, P3, P4, P5, P6) => T): ScalaOngoingStubbing[T]                 = _thenAnswer(f)
  def thenAnswer[P0, P1, P2, P3, P4, P5, P6, P7](f: (P0, P1, P2, P3, P4, P5, P6, P7) => T): ScalaOngoingStubbing[T]         = _thenAnswer(f)
  def thenAnswer[P0, P1, P2, P3, P4, P5, P6, P7, P8](f: (P0, P1, P2, P3, P4, P5, P6, P7, P8) => T): ScalaOngoingStubbing[T] = _thenAnswer(f)
  def thenAnswer[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9](f: (P0, P1, P2, P3, P4, P5, P6, P7, P8, P9) => T): ScalaOngoingStubbing[T] =
    _thenAnswer(f)
  def thenAnswer[P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10](
      f: (P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10) => T): ScalaOngoingStubbing[T] = _thenAnswer(f)
}
