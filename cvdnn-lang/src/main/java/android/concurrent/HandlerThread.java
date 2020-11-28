package android.concurrent;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

public class HandlerThread {
    private final Handler mInternalHandler;

    public HandlerThread(String name) {
        android.os.HandlerThread handlerThread = new android.os.HandlerThread(name);
        handlerThread.start();
        mInternalHandler = new Handler(handlerThread.getLooper());
    }

    public Handler getHandler() {
        return mInternalHandler;
    }

    /**
     * Returns a new {@link Message Message} from the global message pool. More efficient than creating and
     * allocating new instances. The retrieved message has its handler set to this instance (Message.target == this). If
     * you don't want that facility, just call Message.obtain() instead.
     */
    public final Message obtainMessage() {
        return Message.obtain(mInternalHandler);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what member of the returned Message.
     *
     * @param what Value to assign to the returned Message.what field.
     *
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what) {
        return Message.obtain(mInternalHandler, what);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what and obj members of the returned Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param obj  Value to assign to the returned Message.obj field.
     *
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, Object obj) {
        return Message.obtain(mInternalHandler, what, obj);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what, arg1 and arg2 members of the returned
     * Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param arg1 Value to assign to the returned Message.arg1 field.
     * @param arg2 Value to assign to the returned Message.arg2 field.
     *
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, int arg1, int arg2) {
        return Message.obtain(mInternalHandler, what, arg1, arg2);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what, obj, arg1,and arg2 values on the returned
     * Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param arg1 Value to assign to the returned Message.arg1 field.
     * @param arg2 Value to assign to the returned Message.arg2 field.
     * @param obj  Value to assign to the returned Message.obj field.
     *
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return Message.obtain(mInternalHandler, what, arg1, arg2, obj);
    }

    /**
     * Causes the Runnable r to be added to the message queue. The runnable will be run on the thread to which this
     * handler is attached.
     *
     * @param r The Runnable that will be executed.
     *
     * @return Returns true if the Runnable was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting.
     */
    public final boolean post(Runnable r) {

        return mInternalHandler.post(r);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run at a specific time given by
     * <var>uptimeMillis</var>. <b>The time-base is {@link SystemClock#uptimeMillis}.</b> The runnable will
     * be run on the thread to which this handler is attached.
     *
     * @param r            The Runnable that will be executed.
     * @param uptimeMillis The absolute time at which the callback should run, using the
     *                     {@link SystemClock#uptimeMillis} time-base.
     *
     * @return Returns true if the Runnable was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting. Note that a result of true does not
     * mean the Runnable will be processed -- if the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {

        return mInternalHandler.postAtTime(r, uptimeMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run at a specific time given by
     * <var>uptimeMillis</var>. <b>The time-base is {@link SystemClock#uptimeMillis}.</b> The runnable will
     * be run on the thread to which this handler is attached.
     *
     * @param r            The Runnable that will be executed.
     * @param uptimeMillis The absolute time at which the callback should run, using the
     *                     {@link SystemClock#uptimeMillis} time-base.
     *
     * @return Returns true if the Runnable was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting. Note that a result of true does not
     * mean the Runnable will be processed -- if the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     *
     * @see SystemClock#uptimeMillis
     */
    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {

        return mInternalHandler.postAtTime(r, token, uptimeMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses. The
     * runnable will be run on the thread to which this handler is attached.
     *
     * @param r           The Runnable that will be executed.
     * @param delayMillis The delay (in milliseconds) until the Runnable will be executed.
     *
     * @return Returns true if the Runnable was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting. Note that a result of true does not
     * mean the Runnable will be processed -- if the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        return mInternalHandler.postDelayed(r, delayMillis);
    }

    /**
     * Posts a message to an object that implements Runnable. Causes the Runnable r to executed on the next iteration
     * through the message queue. The runnable will be run on the thread to which this handler is attached. <b>This
     * method is only for use in very special circumstances -- it can easily starve the message queue, cause ordering
     * problems, or have other unexpected side-effects.</b>
     *
     * @param r The Runnable that will be executed.
     *
     * @return Returns true if the message was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting.
     */
    public final boolean postAtFrontOfQueue(Runnable r) {

        return mInternalHandler.postAtFrontOfQueue(r);
    }

    /**
     * Remove any pending posts of Runnable r that are in the message queue.
     */
    public final void removeCallbacks(Runnable r) {
        mInternalHandler.removeCallbacks(r);
    }

    /**
     * Remove any pending posts of Runnable <var>r</var> with Object <var>token</var> that are in the message queue. If
     * <var>token</var> is null, all callbacks will be removed.
     */
    public final void removeCallbacks(Runnable r, Object token) {
        mInternalHandler.removeCallbacks(r, token);
    }

    /**
     * Pushes a message onto the end of the message queue after all pending messages before the current time. It will be
     * received in {@link #handleMessage}, in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting.
     */
    public final boolean sendMessage(Message msg) {
        return sendMessageDelayed(msg, 0);
    }

    /**
     * Sends a Message containing only the what value.
     *
     * @return Returns true if the message was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting.
     */
    public final boolean sendEmptyMessage(int what) {
        return sendEmptyMessageDelayed(what, 0);
    }

    /**
     * Sends a Message containing only the what value, to be delivered after the specified amount of time elapses.
     *
     * @return Returns true if the message was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting.
     *
     * @see #sendMessageDelayed(Message, long)
     */
    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        return sendMessageDelayed(msg, delayMillis);
    }

    /**
     * Sends a Message containing only the what value, to be delivered at a specific time.
     *
     * @return Returns true if the message was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting.
     *
     * @see #sendMessageAtTime(Message, long)
     */

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        return sendMessageAtTime(msg, uptimeMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages before (current time + delayMillis). You will
     * receive it in {@link #handleMessage}, in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting. Note that a result of true does not
     * mean the message will be processed -- if the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return sendMessageAtTime(msg, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages before the absolute time (in milliseconds)
     * <var>uptimeMillis</var>. <b>The time-base is {@link SystemClock#uptimeMillis}.</b> You will receive it
     * in {@link #handleMessage}, in the thread attached to this handler.
     *
     * @param uptimeMillis The absolute time at which the message should be delivered, using the
     *                     {@link SystemClock#uptimeMillis} time-base.
     *
     * @return Returns true if the message was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting. Note that a result of true does not
     * mean the message will be processed -- if the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {

        return mInternalHandler.sendMessageAtTime(msg, uptimeMillis);
    }

    /**
     * Enqueue a message at the front of the message queue, to be processed on the next iteration of the message loop.
     * You will receive it in {@link #handleMessage}, in the thread attached to this handler. <b>This method is only for
     * use in very special circumstances -- it can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @return Returns true if the message was successfully placed in to the message queue. Returns false on failure,
     * usually because the looper processing the message queue is exiting.
     */
    public final boolean sendMessageAtFrontOfQueue(Message msg) {

        return mInternalHandler.sendMessageAtFrontOfQueue(msg);
    }

    /**
     * Remove any pending posts of messages with code 'what' that are in the message queue.
     */
    public final void removeMessages(int what) {
        mInternalHandler.removeMessages(what);
    }

    /**
     * Remove any pending posts of messages with code 'what' and whose obj is 'object' that are in the message queue. If
     * <var>object</var> is null, all messages will be removed.
     */
    public final void removeMessages(int what, Object object) {
        mInternalHandler.removeMessages(what, object);
    }

    /**
     * Remove any pending posts of callbacks and sent messages whose <var>obj</var> is <var>token</var>. If
     * <var>token</var> is null, all callbacks and messages will be removed.
     */
    public final void removeCallbacksAndMessages(Object token) {
        mInternalHandler.removeCallbacksAndMessages(token);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' in the message queue.
     */
    public final boolean hasMessages(int what) {
        return mInternalHandler.hasMessages(what);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' and whose obj is 'object' in the message queue.
     */
    public final boolean hasMessages(int what, Object object) {
        return mInternalHandler.hasMessages(what, object);
    }

    // if we can get rid of this method, the handler need not remember its loop
    // we could instead export a getMessageQueue() method...
    public final Looper getLooper() {

        return mInternalHandler.getLooper();
    }
}
