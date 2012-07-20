package org.mundo.blender;


/**
 * Automatically generated distributed object class for <code>IBlenderEvent</code>.
 * @see org.mundo.blender.IBlenderEvent
 */
public class DoIBlenderEvent extends org.mundo.rt.DoObject implements org.mundo.blender.IBlenderEvent
{
  public DoIBlenderEvent()
  {
  }
  public DoIBlenderEvent(org.mundo.rt.Session session, Object obj) throws org.mundo.rt.RMCException
  {
    _bind(session, obj);
  }
  public DoIBlenderEvent(org.mundo.rt.Channel channel) throws org.mundo.rt.RMCException
  {
    _setPublisher(channel.getSession().publish(channel.getZone(), channel.getName()));
  }
  public DoIBlenderEvent(org.mundo.rt.DoObject o)
  {
    _assign(o);
  }
  public org.mundo.rt.ServerStub _getServerStub()
  {
    return SrvIBlenderEvent._getObject();
  }
  public static DoIBlenderEvent _of(org.mundo.rt.Session session, Object obj)
  {
    DoIBlenderEvent cs=(DoIBlenderEvent)_getDoObject(session, DoIBlenderEvent.class, obj);
    if (cs==null)
    {
      cs=new DoIBlenderEvent(session, obj);
      _putDoObject(session, obj, cs);
    }
    return cs;
  }
  public static DoIBlenderEvent _of(org.mundo.rt.Service s)
  {
    return _of(s.getSession(), s);
  }
  public String _getInterfaceName()
  {
    return "org.mundo.blender.IBlenderEvent";
  }
  public static IBlenderEvent _localObject(IBlenderEvent obj)
  {
    if (obj instanceof org.mundo.rt.DoObject)
    {
      return (IBlenderEvent)((org.mundo.rt.DoObject)obj)._getLocalObject();
    }
    else
    {
      return obj;
    }
  }
  public void buttonPushed(int p0)
  {
    if (localObj!=null) 
    {
      ((org.mundo.blender.IBlenderEvent)localObj).buttonPushed(p0);
      return;
    }
    buttonPushed(p0, SYNC);
  }
  public org.mundo.rt.AsyncCall buttonPushed(int p0, Options opt) 
  {
    org.mundo.rt.TypedMap m=new org.mundo.rt.TypedMap();
    m.putString("ptypes", "i");
    m.putString("rtype", "");
    m.putInt("p0", p0);
    org.mundo.rt.AsyncCall call=new org.mundo.rt.AsyncCall(this, "org.mundo.blender.IBlenderEvent", "buttonPushed", m);
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
  
  public void buttonReleased(int p0)
  {
    if (localObj!=null) 
    {
      ((org.mundo.blender.IBlenderEvent)localObj).buttonReleased(p0);
      return;
    }
    buttonReleased(p0, SYNC);
  }
  public org.mundo.rt.AsyncCall buttonReleased(int p0, Options opt) 
  {
    org.mundo.rt.TypedMap m=new org.mundo.rt.TypedMap();
    m.putString("ptypes", "i");
    m.putString("rtype", "");
    m.putInt("p0", p0);
    org.mundo.rt.AsyncCall call=new org.mundo.rt.AsyncCall(this, "org.mundo.blender.IBlenderEvent", "buttonReleased", m);
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