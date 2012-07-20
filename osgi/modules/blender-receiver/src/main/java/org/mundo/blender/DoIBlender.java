package org.mundo.blender;


/**
 * Automatically generated distributed object class for <code>IBlender</code>.
 * @see org.mundo.blender.IBlender
 */
public class DoIBlender extends org.mundo.rt.DoObject implements org.mundo.blender.IBlender
{
  public DoIBlender()
  {
  }
  public DoIBlender(org.mundo.rt.Session session, Object obj) throws org.mundo.rt.RMCException
  {
    _bind(session, obj);
  }
  public DoIBlender(org.mundo.rt.Channel channel) throws org.mundo.rt.RMCException
  {
    _setPublisher(channel.getSession().publish(channel.getZone(), channel.getName()));
  }
  public DoIBlender(org.mundo.rt.DoObject o)
  {
    _assign(o);
  }
  public org.mundo.rt.ServerStub _getServerStub()
  {
    return SrvIBlender._getObject();
  }
  public static DoIBlender _of(org.mundo.rt.Session session, Object obj)
  {
    DoIBlender cs=(DoIBlender)_getDoObject(session, DoIBlender.class, obj);
    if (cs==null)
    {
      cs=new DoIBlender(session, obj);
      _putDoObject(session, obj, cs);
    }
    return cs;
  }
  public static DoIBlender _of(org.mundo.rt.Service s)
  {
    return _of(s.getSession(), s);
  }
  public String _getInterfaceName()
  {
    return "org.mundo.blender.IBlender";
  }
  public static IBlender _localObject(IBlender obj)
  {
    if (obj instanceof org.mundo.rt.DoObject)
    {
      return (IBlender)((org.mundo.rt.DoObject)obj)._getLocalObject();
    }
    else
    {
      return obj;
    }
  }
  public boolean pushButton(int p0)
  {
    if (localObj!=null) 
    {
      return ((org.mundo.blender.IBlender)localObj).pushButton(p0);
    }
    org.mundo.rt.AsyncCall call=pushButton(p0, SYNC);
    return call.getMap().getBoolean("value");
  }
  public org.mundo.rt.AsyncCall pushButton(int p0, Options opt) 
  {
    org.mundo.rt.TypedMap m=new org.mundo.rt.TypedMap();
    m.putString("ptypes", "i");
    m.putString("rtype", "t");
    m.putInt("p0", p0);
    org.mundo.rt.AsyncCall call=new org.mundo.rt.AsyncCall(this, "org.mundo.blender.IBlender", "pushButton", m);
    call.invoke(opt);
    if (opt==SYNC)
    {
      try
      {
        if (!call.waitForReply())
        {
          throw call.getException();
        }
      }
      catch(RuntimeException x)
      {
        throw x;
      }
      catch(Exception x)
      {
        throw new org.mundo.rt.RMCException("unexpected exception", x);
      }
    }
    return call;
  }
  
  public boolean pushButton(int p0, int p1)
  {
    if (localObj!=null) 
    {
      return ((org.mundo.blender.IBlender)localObj).pushButton(p0, p1);
    }
    org.mundo.rt.AsyncCall call=pushButton(p0, p1, SYNC);
    return call.getMap().getBoolean("value");
  }
  public org.mundo.rt.AsyncCall pushButton(int p0, int p1, Options opt) 
  {
    org.mundo.rt.TypedMap m=new org.mundo.rt.TypedMap();
    m.putString("ptypes", "i,i");
    m.putString("rtype", "t");
    m.putInt("p0", p0);
    m.putInt("p1", p1);
    org.mundo.rt.AsyncCall call=new org.mundo.rt.AsyncCall(this, "org.mundo.blender.IBlender", "pushButton", m);
    call.invoke(opt);
    if (opt==SYNC)
    {
      try
      {
        if (!call.waitForReply())
        {
          throw call.getException();
        }
      }
      catch(RuntimeException x)
      {
        throw x;
      }
      catch(Exception x)
      {
        throw new org.mundo.rt.RMCException("unexpected exception", x);
      }
    }
    return call;
  }
  
  public boolean lightOn(boolean p0)
  {
    if (localObj!=null) 
    {
      return ((org.mundo.blender.IBlender)localObj).lightOn(p0);
    }
    org.mundo.rt.AsyncCall call=lightOn(p0, SYNC);
    return call.getMap().getBoolean("value");
  }
  public org.mundo.rt.AsyncCall lightOn(boolean p0, Options opt) 
  {
    org.mundo.rt.TypedMap m=new org.mundo.rt.TypedMap();
    m.putString("ptypes", "t");
    m.putString("rtype", "t");
    m.putBoolean("p0", p0);
    org.mundo.rt.AsyncCall call=new org.mundo.rt.AsyncCall(this, "org.mundo.blender.IBlender", "lightOn", m);
    call.invoke(opt);
    if (opt==SYNC)
    {
      try
      {
        if (!call.waitForReply())
        {
          throw call.getException();
        }
      }
      catch(RuntimeException x)
      {
        throw x;
      }
      catch(Exception x)
      {
        throw new org.mundo.rt.RMCException("unexpected exception", x);
      }
    }
    return call;
  }
  
}