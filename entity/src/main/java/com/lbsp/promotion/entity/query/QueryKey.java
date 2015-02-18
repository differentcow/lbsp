package com.lbsp.promotion.entity.query;

public class QueryKey{

	public enum Operators {
		EQ {
			public String value() {
				return "=";
			}
		},
		GT {
			@Override
			public String value() {
				return ">";
			}
		},
		LT {
			@Override
			public String value() {
				return "<";
			}
		},
		GTE {
			@Override
			public String value() {
				return ">=";
			}
		},
		LTE {
			@Override
			public String value() {
				return "<=";
			}
		},
		NEQ {
			@Override
			public String value() {
				return "!=";
			}
		},
		PRELIKE {
			@Override
			public String value() {
				return "prelike";
			}
		},
        LASTLIKE {
            @Override
            public String value() {
                return "lastlike";
            }
        },
        DEFINELIKE {
            @Override
            public String value() {
                return "definelike";
            }
        },
        LIKE {
            @Override
            public String value() {
                return "like";
            }
        },
		IN {
			@Override
			public String value() {
				return "in";
			}
		},
        NOTIN {
            @Override
            public String value() {
                return "not in";
            }
        },
		ISNULL{
			@Override
			public String value() {
				return "is null";
			}
			
		},
		NOTNULL{
			@Override
			public String value() {
				return "is not null";
			}
			
		};
		public abstract String value();
	}
	public enum Suffix {
		AND {
			public String value() {
				return "and";
			}
		},
		OR {
			@Override
			public String value() {
				return "or";
			}
		},
		L_KH_OR{
			@Override
			public String value() {
				return "( or";
			}
		},
		R_KH_OR{
			@Override
			public String value() {
				return ") or";
			}
		},
		AND_L_KH {
			public String value() {
				return " and (";
			}
		},
		L_KH {
			public String value() {
				return "(";
			}
		},
		AND_R_KH {
			@Override
			public String value() {
				return ") and";
			}
		},
		R_KH {
			@Override
			public String value() {
				return ")";
			}
		},
		EMPTY{
			@Override
			public String value() {
				return " ";
			}
		};
		public abstract String value();
	}
	public enum Prefix {
		AND {
			public String value() {
				return " and ";
			}
		},
		L_KH {
			public String value() {
				return " (";
			}
		},
		R_KH {
			public String value() {
				return ") ";
			}
		},
		EMPTY{
			@Override
			public String value() {
				return " ";
			}
		};
		public abstract String value();
	}
	public QueryKey(String key){
		super();
		this.key = key;
		this.operator = Operators.EQ;
	}
	public QueryKey(String key, Operators operator) {
		super();
		this.key = key;
		this.operator = operator;
	}
	public QueryKey(String key, Operators operator,Suffix suffix) {
		super();
		this.key = key;
		this.operator = operator;
		this.suffix = suffix;
	}
	public QueryKey(String key, Operators operator,Suffix suffix,Prefix prefix) {
		super();
		this.key = key;
		this.operator = operator;
		this.suffix = suffix;
		this.prefix = prefix;
	}
	public QueryKey(String key, Operators operator,Prefix prefix) {
		super();
		this.key = key;
		this.operator = operator;
		this.prefix = prefix;
	}

	private String key;

	private Operators operator;
	
	private Suffix suffix = Suffix.AND;
	
	private Prefix prefix = Prefix.EMPTY;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Operators getOperator() {
		return operator;
	}

	public void setOperator(Operators operator) {
		this.operator = operator;
	}
	public Suffix getSuffix() {
		return suffix;
	}
	public void setSuffix(Suffix suffix) {
		this.suffix = suffix;
	}
	public Prefix getPrefix() {
		return prefix;
	}
	public void setPrefix(Prefix prefix) {
		this.prefix = prefix;
	}
}
