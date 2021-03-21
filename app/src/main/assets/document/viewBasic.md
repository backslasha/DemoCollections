### View 的事件体系

#### 1. view 的一些常见位置属性
- mLeft\mRight\mTop\mBottom 4个属性在 layout 完成赋值，表示 view 在父 layout 中的坐标，赋值后不再改变
- mTranslationX\mTranslationY 代表 view 在 layout 坐标基础上的平移动，和上面4个属性共同决定 view 的位置
- mX\mY 这两个属性描述当前 view 的位置，mX = mLeft + mTranslationX, mY = mTop + mTranslationY
- mScrollX\mScrollY 比较特殊，它不影响 view 的位置，但可以影响 view 的"内容"的位置，并且它的取值正负情况有点反直觉。
mScrollX 取正值时（如100），我们看到的 view 的内容实际上是往左边去移动的。这里可以理解为网页的scrollbar，当你拽住
scrollbar向右拖动时，scrollX是正值，但你看到的网页内容却是向左移动的；竖直方向的同理。因此，scrollX 、scrollY
可以理解为对scrollbar拖动距离描述。

#### 2. 事件分发机制
- 伪代码表示
```
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean consume = false;
        if (onInterceptTouchEvent(ev)) {
            consume = onTouchEvent(ev);
        } else {
            consume = child.dispatchTouchEvent(ev);
        }
        return consume;
    }
```
- 事件流向：从 Activity -> PhoneWindow -> DecorView -> ViewGroup -> View；
- 不管是 View 还是 ViewGroup，onTouchEvent 由 dispatchTouchEvent 调用并决定是否下发；
- ViewGroup 的 dispatchTouchEvent 中有 onInterceptTouchEvent 逻辑，返回 true 时拦截该事件并调用自身 onTouchEvent 处理；
- View 可以使用 requestDisallowInterceptTouchEvent 方法禁止父布局拦截 ACTION_DOWN 以外的事件；
- TouchListener 优先级高于 onTouchEvent，若 TouchListener#onTouch 返回了 true 则 onTouchEvent 不再调用；
- 事件处理结果最终回到 Activity 处，若没有子 View 消费该事件，则 Activity 回调 onTouchEvent.

### 3. 处理滑动冲突
- 外部拦截法：通过 ViewGroup#onInterceptTouchEvent 在需要事件时拦截
- 内部拦截法：父布局默认拦截 ACTION_DOWN 之外的所有事件，子 View 通过 View#requestDisallowInterceptTouchEvent 禁止/启动父布局拦截事件