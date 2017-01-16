<h4>写在前面的话：</h4> 在正常项目流程中，我们很多情况下会碰到点击显示更多文本，这样可以利于页面变化加载，点击显示更多可能会非常常用，现在博主利用自己的闲暇时间来一点一点完成一个自定义控件，这个控件可以满足大多数情况的需求。
<h4>思路：</h4>在写程序的时候，最需要的是思路，好的思路是成功的一半，我们来看看我们的最基本的需求效果：
<b>1、需要在文字特别多的情况下显示只有确定的行数</b>
<b>2、点击右侧图片将所有的文字显示出来</b>
<b>3、文字在左侧覆盖大部分布局，图标在右侧点击显示更多</b>
<b>4、显示的文本不会因为重用优化视图从而发生状态错位</b>
<h4>实现需求:</h4><b>1、继承LinearLayout：</b>
`` public class ExpandableContainer extends LinearLayout {  
    //继承线性布局的好处是可以由系统将我们的两个view进行线性分配，可控制的图形大小以及可变化的view的填充情况
     } ``

<b>2、根据Textview的即textview.setEllipsize()与textview.setMaxLines两个方法重绘View达到显示更多的效果：</b>

      /**  
      *进行重绘view
      */
    private void onresfreshView() {
        if (isExpanded) {
            textView.setEllipsize(null);
            textView.setMaxLines(Integer.MAX_VALUE);
            initView();
        } else {
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(lines);
            initView();
        }
    }

<b>3、在多条目布局的情况下显示状态会让该布局的显示状态发生显示乱位，于是用自带内存的方式来解决这一问题</b>
  
    /**
     *在listview , gridview, recyclerview的条目中使用此方法，防止重绘布局
     * @param text 你所要填充的文本
     * @param position 当前控件所在的position
     */
    public synchronized void setText(String text, int position) {
        this.position = position;
        this.text = text;
        initView();
        if (null!=map&&map.size()>0&&map.keySet().contains(position)){
            isExpanded = map.get(position);
        }else isExpanded=false;
        map.put(position,isExpanded);
        onresfreshView();
    }
<b>4、使用软引用来做防止内存泄漏，在view移除的时候做clear清理对象，重写view的最终销毁方法onDetachedFromWindow在里面进行清理静态对象防止内存泄漏</b>
     
     /**
     * 用软引用避免内存泄露
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mapSoftReference.clear();
    }

<b>5、以上就是所有的view的最重要的几个方法，根据这些方法的自定义的使用，能够很好的完成我们的预期效果：</b>

![预览效果.gif](http://upload-images.jianshu.io/upload_images/4253553-c12437f7c9cbe972.gif?imageMogr2/auto-orient/strip)
