package technology.sola.engine.graphics.guiv2.elements.layout;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;

public class VerticalBoxStyles extends BaseStyles {
  private final int spacing;
  private final Alignment horizontalAlignment;
  private final Alignment verticalAlignment;

  public VerticalBoxStyles(Builder<?> builder) {
    super(builder);
    this.spacing = builder.spacing;
    this.horizontalAlignment = builder.horizontalAlignment;
    this.verticalAlignment = builder.verticalAlignment;
  }

  public static Builder<?> create() {
    return new Builder<>();
  }

  public int spacing() {
    return spacing;
  }

  public Alignment horizontalAlignment() {
    return horizontalAlignment;
  }

  public Alignment verticalAlignment() {
    return verticalAlignment;
  }

  public static class Builder<Self extends Builder<Self>> extends BaseStyles.Builder<Self> {
    private int spacing = 0;
    private Alignment horizontalAlignment = Alignment.START;
    private Alignment verticalAlignment = Alignment.START;

    protected Builder() {
    }

    @Override
    public VerticalBoxStyles build() {
      return new VerticalBoxStyles(this);
    }

    @SuppressWarnings("unchecked")
    public Self setSpacing(int spacing) {
      this.spacing = spacing;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setHorizontalAlignment(Alignment alignment) {
      this.horizontalAlignment = alignment;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setVerticalAlignment(Alignment alignment) {
      this.verticalAlignment = alignment;
      return (Self) this;
    }
  }

  public enum Alignment {
    START,
    CENTER,
    END,
  }
}
